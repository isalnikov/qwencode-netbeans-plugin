package com.qwencode.netbeans.core;

import com.qwencode.netbeans.model.Command;
import com.qwencode.netbeans.model.CommandContext;
import com.qwencode.netbeans.model.ParsedOutput;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.KeyStroke;
import javax.xml.stream.*;

public class CommandManager {
    private static final Logger LOG = Logger.getLogger(CommandManager.class.getName());
    private static final String CONFIG = "qwencode-commands.xml";
    private final List<Command> commands = new ArrayList<>();
    private final QwenCodeCLI cli = new QwenCodeCLI();

    public CommandManager() { loadDefaultCommands(); }

    private void loadDefaultCommands() {
        commands.add(new Command("review", "Code Review", "qwen review --verbose",
            "Analyze code", CommandContext.CURRENT_FILE,
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                java.awt.event.KeyEvent.CTRL_DOWN_MASK | java.awt.event.KeyEvent.ALT_DOWN_MASK)));
        commands.add(new Command("generate", "Generate", "qwen generate",
            "Generate code", CommandContext.SELECTED_CODE,
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
                java.awt.event.KeyEvent.CTRL_DOWN_MASK | java.awt.event.KeyEvent.ALT_DOWN_MASK)));
        commands.add(new Command("refactor", "Refactor", "qwen refactor",
            "Refactor code", CommandContext.SELECTED_CODE,
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T,
                java.awt.event.KeyEvent.CTRL_DOWN_MASK | java.awt.event.KeyEvent.ALT_DOWN_MASK)));
        commands.add(new Command("tests", "Generate Tests", "qwen generate tests",
            "Generate tests", CommandContext.CURRENT_FILE,
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y,
                java.awt.event.KeyEvent.CTRL_DOWN_MASK | java.awt.event.KeyEvent.ALT_DOWN_MASK)));
    }

    public void loadCommands() {
        try {
            Path p = getConfigPath();
            if (Files.exists(p)) {
                commands.clear();
                loadFromXml(p);
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Load failed, using defaults", e);
        }
    }

    public void saveCommands() {
        try { saveToXml(getConfigPath()); }
        catch (Exception e) { LOG.log(Level.SEVERE, "Save failed", e); }
    }

    public List<Command> getCommands() { return new ArrayList<>(commands); }
    public Command getCommand(String id) {
        for (Command c : commands) if (c.getId().equals(id)) return c;
        return null;
    }

    public void addCommand(Command c) { commands.add(c); saveCommands(); }
    public void updateCommand(Command c) {
        for (int i = 0; i < commands.size(); i++)
            if (commands.get(i).getId().equals(c.getId())) { commands.set(i, c); saveCommands(); return; }
    }
    public void deleteCommand(String id) { commands.removeIf(c -> c.getId().equals(id)); saveCommands(); }
    public void restoreDefaults() { commands.clear(); loadDefaultCommands(); saveCommands(); }

    public CompletableFuture<ParsedOutput> executeCommand(Command cmd, ProjectContext ctx, OutputListener l) {
        String full = buildCommand(cmd, ctx);
        if (ctx != null && ctx.getProjectPath() != null) cli.setWorkingDirectory(ctx.getProjectPath());
        return cli.executeAsync(full, l);
    }

    private String buildCommand(Command cmd, ProjectContext ctx) {
        StringBuilder sb = new StringBuilder(cmd.getCliCommand());
        if (ctx != null) {
            switch (cmd.getContext()) {
                case CURRENT_FILE:
                    if (ctx.hasCurrentFile()) sb.append(" ").append(ctx.getCurrentFile().getPath());
                    break;
                case SELECTED_FILES:
                    for (String p : ctx.getSelectedFilePaths()) sb.append(" ").append(p);
                    break;
            }
        }
        return sb.toString();
    }

    private Path getConfigPath() throws IOException {
        Path d = Path.of(System.getProperty("user.home"), ".qwencode", "netbeans");
        if (!Files.exists(d)) Files.createDirectories(d);
        return d.resolve(CONFIG);
    }

    private void loadFromXml(Path p) throws Exception {
        try (FileInputStream fis = new FileInputStream(p.toFile())) {
            XMLStreamReader r = XMLInputFactory.newInstance().createXMLStreamReader(fis);
            while (r.hasNext()) {
                r.next();
                if (r.isStartElement() && "command".equals(r.getLocalName())) {
                    Command c = parseCommand(r);
                    if (c != null) commands.add(c);
                }
            }
        }
    }

    private Command parseCommand(XMLStreamReader r) throws XMLStreamException {
        String id = r.getAttributeValue(null, "id");
        String name = null, cli = null, desc = "", ctx = "PROJECT_ROOT", shortcut = null;
        while (r.hasNext()) {
            r.next();
            if (r.isEndElement() && "command".equals(r.getLocalName())) break;
            if (r.isStartElement()) {
                switch (r.getLocalName()) {
                    case "name": name = r.getElementText(); break;
                    case "cliCommand": cli = r.getElementText(); break;
                    case "description": desc = r.getElementText(); break;
                    case "context": 
                        ctx = r.getElementText(); 
                        try {
                            CommandContext.valueOf(ctx);
                        } catch (IllegalArgumentException e) {
                            ctx = "PROJECT_ROOT";
                        }
                        break;
                    case "shortcut": shortcut = r.getElementText(); break;
                }
            }
        }
        if (id != null && name != null && cli != null) {
            KeyStroke ks = null;
            if (shortcut != null) {
                try { ks = KeyStroke.getKeyStroke(shortcut); } catch (Exception e) {}
            }
            return new Command(id, name, cli, desc, CommandContext.valueOf(ctx), ks);
        }
        return null;
    }

    private void saveToXml(Path p) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(p.toFile())) {
            XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(fos);
            w.writeStartDocument("UTF-8", "1.0");
            w.writeStartElement("commands");
            for (Command c : commands) writeCommand(w, c);
            w.writeEndElement();
            w.writeEndDocument();
            w.close();
        }
    }

    private void writeCommand(XMLStreamWriter w, Command c) throws XMLStreamException {
        w.writeStartElement("command");
        w.writeAttribute("id", c.getId());
        writeElem(w, "name", c.getName());
        writeElem(w, "cliCommand", c.getCliCommand());
        writeElem(w, "description", c.getDescription());
        writeElem(w, "context", c.getContext().name());
        if (c.getShortcut() != null) writeElem(w, "shortcut", c.getShortcut().toString());
        w.writeEndElement();
    }

    private void writeElem(XMLStreamWriter w, String n, String v) throws XMLStreamException {
        w.writeStartElement(n);
        w.writeCharacters(v != null ? v : "");
        w.writeEndElement();
    }
}
