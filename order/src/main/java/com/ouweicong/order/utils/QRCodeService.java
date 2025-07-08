package com.ouweicong.order.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

@Service
public class QRCodeService {
    public byte[] generateQRCodeImage(String content) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);

        // 使用BufferedImage和ByteArrayOutputStream将BitMatrix转换为字节数组
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        bufferedImage = cropImage(bufferedImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        byte[] imageInByte = baos.toByteArray();

        baos.flush();
        baos.close();

        return imageInByte;
    }

    public BufferedImage cropImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // 假设我们要裁剪掉边框，只保留中间部分
        int borderSize = 30; // 你可以根据需要调整这个值
        int croppedWidth = width - 2 * borderSize;
        int croppedHeight = height - 2 * borderSize;

        // 计算裁剪区域的左上角坐标
        int x = borderSize;
        int y = borderSize;

        // 创建一个新的BufferedImage来保存裁剪后的图像
        BufferedImage croppedImage = new BufferedImage(croppedWidth, croppedHeight, originalImage.getType());

        // 使用Graphics2D来绘制裁剪后的图像
        Graphics2D g2d = croppedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, croppedWidth, croppedHeight, x, y, x + croppedWidth, y + croppedHeight, null);
        g2d.dispose();

        return croppedImage;
    }
}
