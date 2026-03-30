package com.qwencode.netbeans.core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

public class ProjectContext {
    private final Project project;
    private final List<FileObject> selectedFiles;
    private final FileObject currentFile;
    private final String selectedText;

    public ProjectContext(Project project, List<FileObject> selectedFiles,
                          FileObject currentFile, String selectedText) {
        this.project = project;
        this.selectedFiles = selectedFiles != null ? selectedFiles : new ArrayList<>();
        this.currentFile = currentFile;
        this.selectedText = selectedText != null ? selectedText : "";
    }

    public String getProjectPath() {
        if (project == null) return null;
        FileObject dir = project.getProjectDirectory();
        return dir.getPath();
    }

    public List<FileObject> getSelectedFiles() { return new ArrayList<>(selectedFiles); }
    public FileObject getCurrentFile() { return currentFile; }
    public String getSelectedText() { return selectedText; }
    public boolean hasSelection() { return selectedText != null && !selectedText.isEmpty(); }
    public boolean hasSelectedFiles() { return !selectedFiles.isEmpty(); }
    public boolean hasCurrentFile() { return currentFile != null; }

    public List<String> getSelectedFilePaths() {
        List<String> paths = new ArrayList<>();
        for (FileObject f : selectedFiles) paths.add(f.getPath());
        return paths;
    }

    public static ProjectContext fromCurrentState() {
        Project project = Lookup.getDefault().lookup(Project.class);
        List<FileObject> selected = new ArrayList<>();
        for (FileObject f : Lookup.getDefault().lookupAll(FileObject.class)) selected.add(f);
        FileObject current = getCurrentFileFromEditor();
        String text = getSelectedTextFromEditor();
        return new ProjectContext(project, selected, current, text);
    }

    private static FileObject getCurrentFileFromEditor() {
        JTextComponent editor = EditorRegistry.lastFocusedComponent();
        if (editor == null) return null;
        Object prop = editor.getDocument().getProperty(DataObject.class);
        DataObject dob = prop instanceof DataObject ? (DataObject) prop : null;
        return dob != null ? dob.getPrimaryFile() : null;
    }

    private static String getSelectedTextFromEditor() {
        JTextComponent editor = EditorRegistry.lastFocusedComponent();
        if (editor == null) return "";
        String t = editor.getSelectedText();
        return t != null ? t : "";
    }
}
