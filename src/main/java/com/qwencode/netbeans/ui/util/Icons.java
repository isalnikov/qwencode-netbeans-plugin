package com.qwencode.netbeans.ui.util;

import javax.swing.ImageIcon;

public class Icons {
    public static ImageIcon getQwenCodeIcon() {
        java.net.URL url = Icons.class.getResource("com/qwencode/netbeans/icons/qwen.png");
        return url != null ? new ImageIcon(url) : null;
    }
}
