package com.qwencode.netbeans.core;

public interface OutputListener {
    void onOutput(String line);
    void onError(String line);
    void onCompleted(boolean success, long durationMs);
}
