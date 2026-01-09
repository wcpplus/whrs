package org.farm2.tools.files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Farm2ImgUtils {
    public static int getImageWidth(File imageFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        return bufferedImage.getWidth();
    }

}
