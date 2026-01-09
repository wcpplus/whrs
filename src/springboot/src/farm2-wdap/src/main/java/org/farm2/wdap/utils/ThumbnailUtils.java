package org.farm2.wdap.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * 缩略图工具类
 */
public class ThumbnailUtils {

    public static void creatThumbnail(int width, File imgFile, File thumbnailFile) {
        try {
            thumbnailFile.getParentFile().mkdirs();
            Thumbnails.of(imgFile)
                    .size(width, width)
                    .toFile(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
