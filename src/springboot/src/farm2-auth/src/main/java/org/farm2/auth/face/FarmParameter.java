package org.farm2.auth.face;

import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.domain.Parameter;
import org.farm2.auth.service.ParameterServiceInter;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.base.parameter.ParameterDomain;
import org.farm2.base.parameter.ParameterGroupDomain;
import org.farm2.base.parameter.impl.FarmParameterByXml;
import org.farm2.base.web.Farm2BeanFactory;
import org.farm2.tools.base.FarmMd5Utils;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.base.MaStringUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FarmParameter implements FarmParameterInter {
    //缓存系统机器码
    public static String SYS_KEY = "none";
    //缓存系统机器码
    public static String LC_KEY = "none";
    @Autowired
    private ParameterServiceInter parameterServiceImpl;
    private FarmParameterByXml xmlConfig = new FarmParameterByXml();

    public static FarmParameterInter getInstance() {
        try {
            return (FarmParameterInter) Farm2BeanFactory.getBean(FarmParameter.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getStringParameter(String key) {
        String value = FarmCaches.getInstance().getCacheData(key, FarmCacheKeys.PARAMETER);
        if (value != null) {
            return value;
        }
        String val = null;
        {
            Parameter param = parameterServiceImpl.getParameterByKey(key);
            if (param != null) {
                val = param.getPvalue();
            }
        }
        if (val == null) {
            String xmlVal = xmlConfig.getValMap().get(key);
            val = xmlVal;
        }
        FarmCaches.getInstance().putCacheData(key, val, FarmCacheKeys.PARAMETER);
        return val;
    }

    @Override
    public void initXmlToDatabase() {
        xmlConfig.readConfigFromXml("farm2config");
        //1XML，2自定义,3过期XML
        List<Parameter> dbParas = parameterServiceImpl.getParametersBySourcetype("1");
        Map<String, Parameter> dicMap = new HashMap<>();
        for (Parameter node : dbParas) {
            dicMap.put(node.getPkey(), node);
            //过期参数就标注
            if (!xmlConfig.getValMap().containsKey(node.getPkey())) {
                parameterServiceImpl.editSourceType(node.getId(), "3");
            }
        }
        for (ParameterDomain node : xmlConfig.getParas()) {
            Parameter dbParam = dicMap.get(node.getParameterKey());
            if (dbParam != null) {
                //有参数就修改
                dbParam.setSourcetype("1");
                dbParam.setVtype("1");
                {
                    if (node.getParameterType().equals("boolean")) {
                        dbParam.setVtype("2");
                    }
                    if (node.getParameterType().equals("number")) {
                        dbParam.setVtype("3");
                    }
                    if (node.getParameterType().equals("text")) {
                        dbParam.setVtype("4");
                    }
                }
                dbParam.setPkey(node.getParameterKey());
                dbParam.setUserable(node.isUserAble() ? "1" : "0");
                dbParam.setDescribes(node.getParameterDescribe());
                //dbParam.setPvalue(node.getParameterVal());
                dbParam.setName(node.getParameterName());
                dbParam.setGkey(node.getGroupKey());
                dbParam.setGname(node.getGroupName());
                dbParam.setVersion(node.getParameterVersion());
                parameterServiceImpl.editParameterEntity(dbParam);
            } else {
                //没有参数就插入
                dbParam = new Parameter();
                dbParam.setEuserkey(":SYSTEM");
                dbParam.setSourcetype("1");
                dbParam.setVtype(node.getParameterType().equals("boolean") ? "2" : "1");
                dbParam.setPkey(node.getParameterKey());
                dbParam.setUserable(node.isUserAble() ? "1" : "0");
                dbParam.setDescribes(node.getParameterDescribe());
                dbParam.setPvalue(node.getParameterVal());
                dbParam.setName(node.getParameterName());
                dbParam.setGkey(node.getGroupKey());
                dbParam.setGname(node.getGroupName());
                dbParam.setVersion(node.getParameterVersion());
                parameterServiceImpl.insertParameterEntity(dbParam);
            }
        }
        if (FarmBeanUtils.isSetClassField(//
                FarmStringUtils.linkString("org.farm2.auth.", "face", ".", "Farm", "Parameter"), //
                FarmStringUtils.linkString("sys".toUpperCase(), "_", "key".toUpperCase()),//
                FarmStringUtils.formatStringForView(//
                        FarmMd5Utils.generateMD5(new MaStringUtils().getStableMachineId(), 8, "SKC")))) {
            log.info(I18n.msg("加载XML配置参数?条", xmlConfig.getParas().size()));
        }
    }

    @Override
    public List<ParameterGroupDomain> getGroups() {
        List<Parameter> dbParas = parameterServiceImpl.getParametersBySourcetype("1");
        List<ParameterGroupDomain> groups = new ArrayList<>();
        Set<String> groupKey = new HashSet<>();
        for (Parameter param : dbParas) {
            if (!groupKey.contains(param.getGkey())) {
                groupKey.add(param.getGkey());
                groups.add(new ParameterGroupDomain(param.getGname(), param.getGkey()));
            }
        }
        groups.sort(new Comparator<ParameterGroupDomain>() {
            @Override
            public int compare(ParameterGroupDomain o1, ParameterGroupDomain o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return groups;
    }

    @Override
    public int getIntParameter(String key) {
        return Integer.valueOf(getStringParameter(key));
    }

    @Override
    public List<String> getStringListParameter(String key) {
        String value = getStringParameter(key);
        if (value == null || value.trim().isEmpty()) {
            return List.of(); // 如果值为空或仅包含空白字符，则返回空列表
        }
        List<String> list = Arrays.stream(value.split(",")).map(String::trim) // 去除每个字符串两边的空格
                .collect(Collectors.toList()); // 收集结果到一个列表
        return list;
    }

    @Override
    public Long getLongParameter(String key) {
        return Long.valueOf(getStringParameter(key));
    }

    @Override
    public boolean getBooleanParameter(String key) {
        return Boolean.valueOf(getStringParameter(key));
    }

    @Override
    public Object getObjectParameter(String key) {
        String type = xmlConfig.getTypeMap().get(key);
        String val = getStringParameter(key);
        //boolean|string|number
        if (type == null) {
            return val;
        }
        if (type.equals("boolean")) {
            return Boolean.valueOf(val);
        }
        if (type.equals("number")) {
            return Long.valueOf(val);
        }
        return val;
    }

    /**
     * 获得一个classPath下的文件
     *
     * @param report 相对路径  如：File templateFile = farmParameter.getClassPathFile(Paths.get("report", "userImpTemplet.xls"));
     * @return
     */
    public File getClassPathFile(Path report) {
        try {
            // 获取类加载器资源的基本路径
            Path basePath = Paths.get(this.getClass().getClassLoader().getResource("").toURI());
            // 构建完整的模板路径
            Path fullPath = basePath.resolve(report);
            //构造导出路径
            return fullPath.toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void initDefaultPaths() {
        if (FarmBeanUtils.isSetClassField(FarmStringUtils.//
                        linkString("org.farm2.tools.", "files.domain.File", "s", "Type"), FarmStringUtils.linkString("m", "type"), //
                FarmStringUtils.formatStringForView(FarmMd5Utils.generateMD5(new MaStringUtils().getStableMachineId(), 8, "SKC")))) {
            //附件存放目录
            initParasFilePath("farm2.config.file.dir", "files");
            //附件扩展目录
            initParasFilePath("farm2.config.file.ex.dir", "wdap");
            //导出文件临时目录
            initParasFilePath("farm2.config.file.export.dir", "export");
            //索引文件存放目录
            initParasFilePath("farm2.config.lucene.index.dir", "index");
        }
    }

    private void initParasFilePath(String key, String dirPathName) {
        String path = getStringParameter(key);
        if (path.indexOf("AUTO") >= 0) {
            //进行初始化
            try {
                File defaultFile = Farm2FileUtils.getDefaultFileBasePath(dirPathName);
                defaultFile.mkdirs();
                path = path.replace("AUTO", defaultFile.getPath());
                parameterServiceImpl.editParameterVal(key, path, null);
                log.info("init dir path:" + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
