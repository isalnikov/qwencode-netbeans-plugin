package com.qwencode.netbeans.core;

import com.qwencode.netbeans.model.ParsedOutput;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QwenCodeCLI {
    private static final Logger LOG = Logger.getLogger(QwenCodeCLI.class.getName());
    private static final ExecutorService EXEC = Executors.newCachedThreadPool();

    private String workingDirectory;
    private Process currentProcess;
    private volatile boolean cancelled;

    public QwenCodeCLI() {
        this.workingDirectory = System.getProperty("user.dir");
    }

    public void setWorkingDirectory(String path) {
        this.workingDirectory = path;
    }

    public CompletableFuture<ParsedOutput> executeAsync(String cmd, OutputListener listener) {
        return CompletableFuture.supplyAsync(() -> execute(cmd, listener), EXEC);
    }

    public ParsedOutput execute(String cmd, OutputListener listener) {
        LocalDateTime start = LocalDateTime.now();
        StringBuilder out = new StringBuilder();
        boolean success = true;
        String error = "";

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s+"));
            pb.directory(new File(workingDirectory));
            currentProcess = pb.start();

            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(currentProcess.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null && !cancelled) {
                    out.append(line).append("\n");
                    if (listener != null) listener.onOutput(line);
                }
            }

            try (BufferedReader er = new BufferedReader(
                    new InputStreamReader(currentProcess.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = er.readLine()) != null && !cancelled) {
                    out.append(line).append("\n");
                    if (listener != null) listener.onError(line);
                }
            }

            int exit = currentProcess.waitFor();
            if (exit != 0) {
                success = false;
                error = "Exit code: " + exit;
            }

        } catch (IOException e) {
            success = false;
            error = "IO error: " + e.getMessage();
            LOG.log(Level.SEVERE, "Command error", e);
        } catch (InterruptedException e) {
            success = false;
            error = "Interrupted";
            Thread.currentThread().interrupt();
        } finally {
            currentProcess = null;
        }

        Duration duration = Duration.between(start, LocalDateTime.now());
        if (listener != null) listener.onCompleted(success, duration.toMillis());

        ParsedOutput.Builder builder = ParsedOutput.builder()
            .command(cmd)
            .workingDirectory(workingDirectory)
            .consoleOutput(out.toString())
            .executionTime(duration);
        
        if (!success) {
            builder.errorMessage(error);
        }
        
        return builder.build();
    }

    public void cancelExecution() {
        cancelled = true;
        if (currentProcess != null) currentProcess.destroyForcibly();
    }

    public void resetCancel() {
        cancelled = false;
    }

    public static boolean isInstalled() {
        try {
            Process p = new ProcessBuilder("qwen", "--version").start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
