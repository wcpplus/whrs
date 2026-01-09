package org.farm2.wdap.utils;

import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.domain.WdapExtendFile;

import java.util.*;

/**
 * 附件扩展模型相关工具
 */
public class FileModelUtils {
    private static Map<String, FileModel> fileModels = new HashMap<>();

    /**
     * 注册扩展模型
     *
     * @param model
     */
    public static void registFileModel(FileModel model) {
        fileModels.put(model.getKey(), model);
    }

    public static FileModel getModel(String filemodel) {
        return fileModels.get(filemodel);
    }

    public static List<FileModel> getModels() {
        List<FileModel> list = new ArrayList<>();
        for (FileModel fm : fileModels.values()) {
            list.add(fm);
        }
        return list;
    }

    /**
     * 获得最棒的预览效果
     *
     * @param exfiles 文件列表
     * @return 最佳预览文件
     */
    public static WdapExtendFile getBestView(List<WdapExtendFile> exfiles) {
        if (exfiles == null || exfiles.isEmpty()) {
            return null; // 如果没有文件，直接返回 null
        }
        List<FileModel> fileModels = getModels();
        String sortRule = "IMGS_FILE,HTML_FILE,PDF_FILE,TEXT_FILE,VIDEO_MP4_FILE,AUDIO_MP3_FILE,THUMBNAIL_FILE";
        // 将 sortRule 转换为 Map，方便快速查找优先级
        Map<String, Integer> priorityMap = new LinkedHashMap<>();
        String[] rules = sortRule.split(",");
        for (int i = 0; i < rules.length; i++) {
            priorityMap.put(rules[i], i); // 优先级从 0 开始
        }
        // 对 fileModels 按照 sortRule 排序
        fileModels.sort(Comparator.comparing(model -> priorityMap.getOrDefault(model.getKey(), Integer.MAX_VALUE)));
        // 遍历 exfiles，找到与 fileModels 最匹配的文件
        for (FileModel model : fileModels) {
            for (WdapExtendFile exfile : exfiles) {
                if (exfile.getFilemodel().equals(model.getKey())) {
                    return exfile; // 返回第一个匹配的文件
                }
            }
        }
        return null; // 如果没有匹配的文件，返回 null
    }
}
