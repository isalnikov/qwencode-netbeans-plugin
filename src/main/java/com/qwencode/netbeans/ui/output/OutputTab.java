package com.qwencode.netbeans.ui.output;

public enum OutputTab {
    CONSOLE("Console"),
    ISSUES("Issues"),
    SUGGESTIONS("Suggestions"),
    CHANGES("Changes");

    private final String displayName;

    OutputTab(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
