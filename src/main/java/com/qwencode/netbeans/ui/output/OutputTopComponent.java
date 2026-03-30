package com.qwencode.netbeans.ui.output;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Output window for QwenCode commands.
 * TODO: Full implementation in Task 7
 */
public class OutputTopComponent extends TopComponent {
    
    private static OutputTopComponent instance;
    
    public OutputTopComponent() {
        this("QwenCode Output");
    }
    
    private OutputTopComponent(String name) {
        setName(name);
        setToolTipText("QwenCode command output");
    }
    
    public static OutputTopComponent getInstance() {
        if (instance == null) {
            instance = new OutputTopComponent();
        }
        return instance;
    }
    
    public void startCommand(String commandName, String projectPath) {
        // TODO: Implement
    }
    
    public void appendConsole(String line) {
        // TODO: Implement
    }
    
    public void appendError(String line) {
        // TODO: Implement
    }
    
    public void completeCommand(boolean success, long duration) {
        // TODO: Implement
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    
    @Override
    public void componentOpened() {
        if (instance == null) {
            instance = this;
        }
    }
    
    @Override
    public void componentClosed() {
        if (instance == this) {
            instance = null;
        }
    }
}
