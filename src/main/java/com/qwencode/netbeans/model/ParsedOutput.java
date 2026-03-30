package com.qwencode.netbeans.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParsedOutput {
    private final String command;
    private final String workingDirectory;
    private final String consoleOutput;
    private final List<OutputItem> items;
    private final Duration executionTime;
    private final boolean successful;
    private final String errorMessage;

    private ParsedOutput(Builder b) {
        this.command = b.command;
        this.workingDirectory = b.workingDirectory;
        this.consoleOutput = b.consoleOutput;
        this.items = new ArrayList<>(b.items);
        this.executionTime = b.executionTime;
        this.successful = b.successful;
        this.errorMessage = b.errorMessage;
    }

    public String getCommand() { return command; }
    public String getWorkingDirectory() { return workingDirectory; }
    public String getConsoleOutput() { return consoleOutput; }
    public List<OutputItem> getItems() { return Collections.unmodifiableList(items); }
    public List<OutputItem> getIssues() { return filter(OutputItem.ItemType.ISSUE); }
    public List<OutputItem> getSuggestions() { return filter(OutputItem.ItemType.SUGGESTION); }
    public List<OutputItem> getChanges() { return filter(OutputItem.ItemType.CHANGE); }
    public Duration getExecutionTime() { return executionTime; }
    public boolean isSuccessful() { return successful; }
    public String getErrorMessage() { return errorMessage; }

    private List<OutputItem> filter(OutputItem.ItemType type) {
        List<OutputItem> r = new ArrayList<>();
        for (OutputItem i : items) if (i.getType() == type) r.add(i);
        return r;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String command = "";
        private String workingDirectory = "";
        private String consoleOutput = "";
        private List<OutputItem> items = new ArrayList<>();
        private Duration executionTime = Duration.ZERO;
        private boolean successful = true;
        private String errorMessage = "";

        public Builder command(String c) { this.command = c; return this; }
        public Builder workingDirectory(String w) { this.workingDirectory = w; return this; }
        public Builder consoleOutput(String o) { this.consoleOutput = o; return this; }
        public Builder addItem(OutputItem i) { this.items.add(i); return this; }
        public Builder executionTime(Duration t) { this.executionTime = t; return this; }
        public Builder errorMessage(String e) { this.errorMessage = e; this.successful = false; return this; }
        public ParsedOutput build() { return new ParsedOutput(this); }
    }
}
