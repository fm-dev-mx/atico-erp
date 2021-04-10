package com.atico.erp.core.backend.classes;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class PdfHelper {

    public static void writeThumbnailImage(String pdfPath, String thumbnailPath) throws IOException {
        File pdfFile = new File(pdfPath);
        RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdf = new PDFFile(buf);
        PDFPage page = pdf.getPage(0);

        // create the image
        Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
                (int) page.getBBox().getHeight());
        BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
                BufferedImage.TYPE_INT_RGB);

        Image image = page.getImage(
                 rect.width, rect.height,    // width & height
                 rect,                       // clip rect
                null,               // null for the ImageObserver
                true,                // fill background with white
                true                    // block until drawing is done
        );
        Graphics2D bufImageGraphics = bufferedImage.createGraphics();
        bufImageGraphics.drawImage(image, 0, 0, null);

        // resize image
        BufferedImage resizedImage = new BufferedImage(396, 306, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(bufferedImage, 0, 0, 396, 306, null);
        graphics2D.dispose();

        ImageIO.write(resizedImage, "PNG", new File( thumbnailPath ));

        bufImageGraphics.dispose();
        channel.close();
        raf.close();
    }

}
