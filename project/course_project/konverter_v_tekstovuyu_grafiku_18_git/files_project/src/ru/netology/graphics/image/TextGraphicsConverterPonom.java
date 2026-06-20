package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterPonom implements TextGraphicsConverter {
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        double imgRatioHorizon = (double) img.getWidth() / img.getHeight();
        double imgRatioVertical = (double) img.getHeight() / img.getWidth();

        if (maxRatio != 0) {
            if (maxRatio < imgRatioHorizon) {
                throw new BadImageSizeException(imgRatioHorizon, maxRatio);
            } else if (maxRatio < imgRatioVertical) {
                throw new BadImageSizeException(imgRatioVertical, maxRatio);
            }
        }

        int newWidth;
        int newHeight;

        double kw = (maxWidth == 0 || maxWidth > img.getWidth()) ? 1 : (double) img.getWidth() / maxWidth;
        double kh = (maxHeight == 0 || maxHeight > img.getHeight()) ? 1 : (double) img.getHeight() / maxHeight;

if (maxRatio == 1) {
    newWidth = newHeight = Math.min(
            (int) Math.round(img.getWidth() / kw),
            (int) Math.round(img.getHeight() / kh)
    );
} else {
    if (kw > kh) {
        newWidth = (int) Math.round(img.getWidth() / kw);
        newHeight = (int) Math.round(img.getHeight() / kw);
    } else {
        newWidth = (int) Math.round(img.getWidth() / kh);
        newHeight = (int) Math.round(img.getHeight() / kh);
    }
}

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);

        ImageIO.write(bwImg, "png", new File("out.png"));

        WritableRaster bwRaster = bwImg.getRaster();

        if (schema == null) {
            schema = new TextColorSchemaPonom();
        }

        char[][] redPixelChar = new char[newHeight][newWidth];
        StringBuilder transPicte = new StringBuilder();

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                redPixelChar[h][w] = c;
                transPicte.append(redPixelChar[h][w]).append("");
            }
            transPicte.append("\n");
        }

        return transPicte.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
