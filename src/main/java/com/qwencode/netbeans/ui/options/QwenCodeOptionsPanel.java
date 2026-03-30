package com.qwencode.netbeans.ui.options;

import com.qwencode.netbeans.core.CommandManager;
import com.qwencode.netbeans.model.Command;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class QwenCodeOptionsPanel extends JPanel {
    private final CommandManager cmdMgr;
    private final QwenCodeOptionsPanelController ctrl;
    private JTable table;
    private DefaultTableModel model;

    public QwenCodeOptionsPanel(CommandManager mgr, QwenCodeOptionsPanelController ctrl) {
        this.cmdMgr = mgr;
        this.ctrl = ctrl;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Custom Commands");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[][]{}, new String[]{"Name", "Command", "Context", "Shortcut"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton add = new JButton("Add...");
        add.addActionListener(e -> addCommand());
        btnPanel.add(add);
        JButton edit = new JButton("Edit...");
        edit.addActionListener(e -> editCommand());
        btnPanel.add(edit);
        JButton del = new JButton("Delete");
        del.addActionListener(e -> deleteCommand());
        btnPanel.add(del);
        JButton restore = new JButton("Restore Defaults");
        restore.addActionListener(e -> restoreDefaults());
        btnPanel.add(restore);
        add(btnPanel, BorderLayout.SOUTH);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editCommand();
            }
        });
    }

    public void load() {
        model.setRowCount(0);
        for (Command c : cmdMgr.getCommands())
            model.addRow(new Object[]{c.getName(), c.getCliCommand(), c.getContextDisplayName(),
                c.getShortcut() != null ? c.getShortcut().toString() : ""});
    }

    public void store() { ctrl.fireChanged(); }
    public boolean valid() { return true; }

    private void addCommand() {
        CommandEditorDialog d = new CommandEditorDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), true, null);
        d.setVisible(true);
        if (d.isConfirmed()) { cmdMgr.addCommand(d.getCommand()); load(); ctrl.fireChanged(); }
    }

    private void editCommand() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Command c = cmdMgr.getCommands().get(row);
        CommandEditorDialog d = new CommandEditorDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), true, c);
        d.setVisible(true);
        if (d.isConfirmed()) { cmdMgr.updateCommand(d.getCommand()); load(); ctrl.fireChanged(); }
    }

    private void deleteCommand() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int r = JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            cmdMgr.deleteCommand(cmdMgr.getCommands().get(row).getId());
            load();
            ctrl.fireChanged();
        }
    }

    private void restoreDefaults() {
        int r = JOptionPane.showConfirmDialog(this, "Restore defaults?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) { cmdMgr.restoreDefaults(); load(); ctrl.fireChanged(); }
    }
}
