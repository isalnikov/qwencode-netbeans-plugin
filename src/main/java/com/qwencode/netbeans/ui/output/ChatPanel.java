package com.qwencode.netbeans.ui.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChatPanel extends JPanel {
    private JPanel messages;
    private JTextField input;

    public ChatPanel() { init(); }

    private void init() {
        setLayout(new BorderLayout());
        messages = new JPanel();
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        messages.setBorder(new EmptyBorder(10, 10, 10, 10));
        messages.setBackground(Color.decode("#1e1e1e"));
        add(new JScrollPane(messages), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        input = new JTextField();
        input.setBackground(Color.decode("#2b2b2b"));
        input.setForeground(Color.decode("#abb2bf"));
        input.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#4e5254")),
            new EmptyBorder(5, 8, 5, 8)));
        inputPanel.add(input, BorderLayout.CENTER);
        JButton send = new JButton("Send");
        send.setBackground(Color.decode("#ffc107"));
        send.setForeground(Color.decode("#2b2b2b"));
        send.addActionListener(e -> sendMessage());
        inputPanel.add(send, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String t = input.getText().trim();
        if (t.isEmpty()) return;
        addMessage(t, Color.decode("#61afef"), "You");
        input.setText("");
    }

    private void addMessage(String text, Color headerColor, String sender) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        JLabel h = new JLabel(sender);
        h.setForeground(headerColor);
        h.setFont(new Font("SansSerif", Font.BOLD, 11));
        p.add(h);
        p.add(Box.createVerticalStrut(4));
        JTextArea ta = new JTextArea(text);
        ta.setBackground(Color.decode("#2b2b2b"));
        ta.setForeground(Color.decode("#abb2bf"));
        ta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setLineWrap(true);
        p.add(ta);
        messages.add(p);
        messages.add(Box.createVerticalStrut(10));
    }
}
