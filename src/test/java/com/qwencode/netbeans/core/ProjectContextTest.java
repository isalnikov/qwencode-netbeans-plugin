package com.qwencode.netbeans.core;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectContextTest {
    @Test
    public void testCreation() {
        ProjectContext ctx = new ProjectContext(null, null, null, null);
        assertFalse(ctx.hasSelection());
        assertFalse(ctx.hasSelectedFiles());
        assertEquals("", ctx.getSelectedText());
    }

    @Test
    public void testWithFiles() {
        org.openide.filesystems.FileObject f1 = org.mockito.Mockito.mock(org.openide.filesystems.FileObject.class);
        org.mockito.Mockito.when(f1.getPath()).thenReturn("/test/file1.java");
        ProjectContext ctx = new ProjectContext(null, Arrays.asList(f1), null, "code");
        assertTrue(ctx.hasSelectedFiles());
        assertTrue(ctx.hasSelection());
        assertEquals(1, ctx.getSelectedFilePaths().size());
    }
}
