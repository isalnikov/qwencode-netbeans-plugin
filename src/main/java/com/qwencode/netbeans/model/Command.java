package com.qwencode.netbeans.model;

import java.util.Objects;
import javax.swing.KeyStroke;

public class Command {
    private final String id;
    private String name;
    private String cliCommand;
    private String description;
    private CommandContext context;
    private KeyStroke shortcut;
    private boolean enabled;

    public Command(String id, String name, String cliCommand, String description,
                   CommandContext context, KeyStroke shortcut) {
        this.id = id;
        this.name = name;
        this.cliCommand = cliCommand;
        this.description = description;
        this.context = context;
        this.shortcut = shortcut;
        this.enabled = true;
    }

    public Command(String id, String name, String cliCommand, CommandContext context) {
        this(id, name, cliCommand, "", context, null);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCliCommand() { return cliCommand; }
    public String getDescription() { return description; }
    public CommandContext getContext() { return context; }
    public KeyStroke getShortcut() { return shortcut; }
    public boolean isEnabled() { return enabled; }

    public void setName(String name) { this.name = name; }
    public void setCliCommand(String cliCommand) { this.cliCommand = cliCommand; }
    public void setDescription(String description) { this.description = description; }
    public void setContext(CommandContext context) { this.context = context; }
    public void setShortcut(KeyStroke shortcut) { this.shortcut = shortcut; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getContextDisplayName() {
        switch (context) {
            case PROJECT_ROOT: return "Project Root";
            case SELECTED_FILES: return "Selected Files";
            case CURRENT_FILE: return "Current File";
            case SELECTED_CODE: return "Selected Code";
            default: return "Unknown";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(id, command.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Command{id='" + id + "',name='" + name + "',context=" + context + '}';
    }
}
