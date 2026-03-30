package com.qwencode.netbeans.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class OutputItem {
    public enum ItemType { ISSUE, SUGGESTION, CHANGE, INFO }

    private final ItemType type;
    private final String title;
    private final String description;
    private final String filePath;
    private final int lineNumber;
    private final String originalCode;
    private final String suggestedCode;
    private final LocalDateTime timestamp;

    private OutputItem(Builder b) {
        this.type = b.type;
        this.title = b.title;
        this.description = b.description;
        this.filePath = b.filePath;
        this.lineNumber = b.lineNumber;
        this.originalCode = b.originalCode;
        this.suggestedCode = b.suggestedCode;
        this.timestamp = b.timestamp;
    }

    public ItemType getType() { return type; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; }
    public int getLineNumber() { return lineNumber; }
    public String getOriginalCode() { return originalCode; }
    public String getSuggestedCode() { return suggestedCode; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ItemType type = ItemType.INFO;
        private String title = "";
        private String description = "";
        private String filePath = "";
        private int lineNumber = -1;
        private String originalCode = "";
        private String suggestedCode = "";
        private LocalDateTime timestamp = LocalDateTime.now();

        public Builder type(ItemType t) { this.type = t; return this; }
        public Builder title(String t) { this.title = t; return this; }
        public Builder description(String d) { this.description = d; return this; }
        public Builder filePath(String f) { this.filePath = f; return this; }
        public Builder lineNumber(int n) { this.lineNumber = n; return this; }
        public Builder originalCode(String c) { this.originalCode = c; return this; }
        public Builder suggestedCode(String c) { this.suggestedCode = c; return this; }
        public OutputItem build() { return new OutputItem(this); }
    }
}
