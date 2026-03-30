package com.qwencode.netbeans.ui.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@TopComponent.Description(preferredID = "OutputTopComponent", persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "com.qwencode.netbeans.ui.output.OutputTopComponent")
@ActionReference(path = "Menu/Window", position = 200)
@Messages({"CTL_OutputTopComponentAction=QwenCode Output", "HINT_OutputTopComponent=QwenCode output"})
public class OutputTopComponent extends TopComponent {
    private JTabbedPane tabs;
    private JTextPane console;
    private SimpleAttributeSet normalAttr, errorAttr, successAttr;

    public OutputTopComponent() { init(); }

    public static OutputTopComponent getInstance() {
        return (OutputTopComponent) WindowManager.getDefault().findTopComponent(OutputTopComponent.class.getName());
    }

    private void init() {
        setLayout(new BorderLayout());
        setDisplayName(Bundle.CTL_OutputTopComponentAction());

        tabs = new JTabbedPane();
        console = new JTextPane();
        console.setEditable(false);
        console.setFont(new Font("Monospaced", Font.PLAIN, 12));
        console.setBackground(Color.decode("#1e1e1e"));
        console.setForeground(Color.decode("#abb2bf"));

        normalAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(normalAttr, Color.decode("#abb2bf"));
        errorAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttr, Color.decode("#e06c75"));
        successAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(successAttr, Color.decode("#98c379"));

        tabs.addTab("Console", new JScrollPane(console));
        tabs.addTab("Issues", new JPanel());
        tabs.addTab("Suggestions", new JPanel());
        tabs.addTab("Changes", new JPanel());
        tabs.addTab("Chat", new ChatPanel());

        add(tabs, BorderLayout.CENTER);
    }

    public void startCommand(String name, String workDir) {
        SwingUtilities.invokeLater(() -> {
            tabs.setSelectedIndex(0);
            appendConsole("════════════════════════════════════════");
            appendConsole("Command: " + name);
            if (workDir != null) appendConsole("Working directory: " + workDir);
            appendConsole("────────────────────────────────────────");
        });
    }

    public void appendConsole(String line) {
        SwingUtilities.invokeLater(() -> {
            try { console.getDocument().insertString(console.getDocument().getLength(), line + "\n", normalAttr); }
            catch (Exception e) {}
        });
    }

    public void appendError(String line) {
        SwingUtilities.invokeLater(() -> {
            try { console.getDocument().insertString(console.getDocument().getLength(), line + "\n", errorAttr); }
            catch (Exception e) {}
        });
    }

    public void completeCommand(boolean success, long ms) {
        SwingUtilities.invokeLater(() -> {
            appendConsole("────────────────────────────────────────");
            appendConsole((success ? "✓" : "✗") + " Completed in " + String.format("%.1fs", ms / 1000.0));
        });
    }
}
