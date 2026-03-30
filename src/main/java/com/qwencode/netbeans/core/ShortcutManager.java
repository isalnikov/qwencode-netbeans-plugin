package com.qwencode.netbeans.core;

import com.qwencode.netbeans.model.Command;
import java.util.HashMap;
import java.util.Map;
import javax.swing.KeyStroke;

public class ShortcutManager {
    private final Map<String, KeyStroke> shortcuts = new HashMap<>();
    private final Map<KeyStroke, String> reverse = new HashMap<>();

    public void registerShortcut(String name, KeyStroke ks, Runnable action) {
        if (ks == null) return;
        String existing = reverse.get(ks);
        if (existing != null && !existing.equals(name))
            System.err.println("Shortcut conflict: " + ks + " for " + existing);
        shortcuts.put(name, ks);
        reverse.put(ks, name);
    }

    public KeyStroke getShortcut(String name) { return shortcuts.get(name); }
    public void unregisterShortcut(String name) {
        KeyStroke ks = shortcuts.remove(name);
        if (ks != null) reverse.remove(ks);
    }
    public boolean isTaken(KeyStroke ks) { return reverse.containsKey(ks); }
    public String getCommand(KeyStroke ks) { return reverse.get(ks); }

    public void loadFromCommands(CommandManager mgr) {
        for (Command c : mgr.getCommands())
            if (c.getShortcut() != null)
                registerShortcut(c.getId(), c.getShortcut(), () -> {});
    }
}
