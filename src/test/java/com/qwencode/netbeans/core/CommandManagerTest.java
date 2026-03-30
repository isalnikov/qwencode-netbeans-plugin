package com.qwencode.netbeans.core;

import com.qwencode.netbeans.model.Command;
import com.qwencode.netbeans.model.CommandContext;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandManagerTest {
    @Test
    public void testDefaults() {
        CommandManager m = new CommandManager();
        List<Command> cmds = m.getCommands();
        assertTrue(cmds.size() >= 4);
        assertNotNull(m.getCommand("review"));
        assertEquals("Code Review", m.getCommand("review").getName());
    }

    @Test
    public void testAdd() {
        CommandManager m = new CommandManager();
        m.addCommand(new Command("c1", "C1", "cmd1", CommandContext.PROJECT_ROOT));
        assertNotNull(m.getCommand("c1"));
    }

    @Test
    public void testUpdate() {
        CommandManager m = new CommandManager();
        Command r = m.getCommand("review");
        r.setName("New Review");
        m.updateCommand(r);
        assertEquals("New Review", m.getCommand("review").getName());
    }

    @Test
    public void testDelete() {
        CommandManager m = new CommandManager();
        m.deleteCommand("review");
        assertNull(m.getCommand("review"));
    }

    @Test
    public void testRestore() {
        CommandManager m = new CommandManager();
        m.deleteCommand("review");
        m.restoreDefaults();
        assertNotNull(m.getCommand("review"));
    }
}
