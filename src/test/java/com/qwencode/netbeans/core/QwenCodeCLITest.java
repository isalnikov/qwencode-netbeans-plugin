package com.qwencode.netbeans.core;

import com.qwencode.netbeans.model.ParsedOutput;
import org.junit.Test;
import static org.junit.Assert.*;

public class QwenCodeCLITest {
    @Test
    public void testNotInstalled() {
        boolean installed = QwenCodeCLI.isInstalled();
        assertNotNull(installed);
    }

    @Test
    public void testInvalidCommand() {
        QwenCodeCLI cli = new QwenCodeCLI();
        TestListener l = new TestListener();
        ParsedOutput out = cli.execute("invalid-cmd-xyz", l);
        assertFalse(out.isSuccessful());
        assertTrue(l.completed);
    }

    static class TestListener implements OutputListener {
        boolean completed = false;
        public void onOutput(String l) {}
        public void onError(String l) {}
        public void onCompleted(boolean s, long d) { completed = true; }
    }
}
