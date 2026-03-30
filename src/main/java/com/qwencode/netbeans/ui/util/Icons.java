package com.qwencode.netbeans.ui.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Icons {
    public static ImageIcon getQwenCodeIcon() {
        java.net.URL url = Icons.class.getResource("/com/qwencode/netbeans/icons/qwen.png");
        if (url != null) {
            return new ImageIcon(url);
        }
        return createFallbackIcon();
    }

    private static ImageIcon createFallbackIcon() {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.YELLOW);
        g.fillOval(2, 2, 12, 12);
        g.setColor(Color.BLACK);
        g.drawString("Q", 4, 12);
        g.dispose();
        return new ImageIcon(img);
    }
}
