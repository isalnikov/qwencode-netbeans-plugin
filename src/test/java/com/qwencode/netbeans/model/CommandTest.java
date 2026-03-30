package com.qwencode.netbeans.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommandTest {
    @Test
    public void testCreation() {
        Command cmd = new Command("id1", "Test", "qwen test", CommandContext.PROJECT_ROOT);
        assertEquals("id1", cmd.getId());
        assertEquals("Test", cmd.getName());
        assertEquals(CommandContext.PROJECT_ROOT, cmd.getContext());
        assertTrue(cmd.isEnabled());
    }

    @Test
    public void testSetters() {
        Command cmd = new Command("id1", "Test", "qwen test", CommandContext.PROJECT_ROOT);
        cmd.setName("Updated");
        cmd.setEnabled(false);
        assertEquals("Updated", cmd.getName());
        assertFalse(cmd.isEnabled());
    }

    @Test
    public void testEquality() {
        Command c1 = new Command("same", "A", "x", CommandContext.PROJECT_ROOT);
        Command c2 = new Command("same", "B", "y", CommandContext.SELECTED_FILES);
        assertEquals(c1, c2);
    }
}
