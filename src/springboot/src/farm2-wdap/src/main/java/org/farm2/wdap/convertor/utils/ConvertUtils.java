package org.farm2.wdap.convertor.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.json.FarmJsons;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.dto.ConvertorImplDto;
import org.farm2.wdap.utils.FileModelUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtils {

    private static List<Class> impls = new ArrayList<>();


    /**
     * 注册实现类class
     *
     * @param convertorImplClass
     */
    public static void registImpl(Class convertorImplClass) {
        impls.add(convertorImplClass);
    }


    /**
     * 获得转换器的实现类
     *
     * @return
     */
    public static List<ConvertorImplDto> getImpls() {
        List<ConvertorImplDto> list = new ArrayList<>();
        for (Class<FileConvertorInter> impl : impls) {
            Constructor<FileConvertorInter> constructor = null; // 获取无参构造函数
            try {
                constructor = impl.getDeclaredConstructor();
                FileConvertorInter obj = constructor.newInstance(); // 创建实例
                ConvertorImplDto dto = new ConvertorImplDto();
                dto.setNote(obj.getNote());
                dto.setClassKey(impl.getName());
                dto.setTitle(obj.getTitle());
                dto.setSFileModels(obj.getSourceFileModel());
                list.add(dto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }


    /**
     * 反射一个类对象
     *
     * @param classKey
     * @return
     */
    public static FileConvertorInter getConvertorImpl(String classKey) {
        Constructor<FileConvertorInter> constructor = null; // 获取无参构造函数
        try {
            constructor = (Constructor<FileConvertorInter>) Class.forName(classKey).getDeclaredConstructor();
            FileConvertorInter obj = constructor.newInstance(); // 创建实例
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得附件模型的翻译字典
     *
     * @return
     */
    public static Map<String, String> getFileModelDic() {
        Map<String, String> map = new HashMap<>();
        for (FileModel model : FileModelUtils.getModels()) {
            map.put(model.getKey(), model.getTitle());
        }
        return map;
    }

    /**
     * 获得转换器参数
     *
     * @param params
     * @param convertorImpl
     * @return
     */
    public static Map<String, ConvertorParam> getParams(String params, FileConvertorInter convertorImpl) {
        List<ConvertorParam> paramList = null;
        if (StringUtils.isNotBlank(params)) {
            paramList = JSON.parseObject(params, new TypeReference<List<ConvertorParam>>() {
            });
        } else {
            paramList = convertorImpl.getParams();
        }
        Map<String, ConvertorParam> map = new HashMap<>();
        for (ConvertorParam p : paramList) {
            map.put(p.getField(), p);
        }
        return map;
    }
}
