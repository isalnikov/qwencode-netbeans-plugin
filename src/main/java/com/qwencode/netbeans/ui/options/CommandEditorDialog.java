package com.qwencode.netbeans.ui.options;

import com.qwencode.netbeans.model.Command;
import com.qwencode.netbeans.model.CommandContext;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;
import javax.swing.*;
import javax.swing.KeyStroke;

public class CommandEditorDialog extends JDialog {
    private final Command existing;
    private Command result;
    private boolean confirmed = false;
    private JTextField nameField, cmdField, descField, shortcutField;
    private JComboBox<CommandContext> ctx_combo;

    public CommandEditorDialog(Frame parent, boolean modal, Command existing) {
        super(parent, modal);
        this.existing = existing;
        setTitle(existing == null ? "Add Command" : "Edit Command");
        init();
        pack();
        setLocationRelativeTo(parent);
    }

    private void init() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        nameField = new JTextField(20);
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("CLI Command:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmdField = new JTextField(20);
        form.add(cmdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        descField = new JTextField(20);
        form.add(descField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        form.add(new JLabel("Context:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        ctx_combo = new JComboBox<>(CommandContext.values());
        form.add(ctx_combo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        form.add(new JLabel("Shortcut:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        shortcutField = new JTextField(15);
        form.add(shortcutField, gbc);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton save = new JButton("Save");
        save.addActionListener(e -> save());
        btnPanel.add(save);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> { confirmed = false; dispose(); });
        btnPanel.add(cancel);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) {
            nameField.setText(existing.getName());
            cmdField.setText(existing.getCliCommand());
            descField.setText(existing.getDescription());
            ctx_combo.setSelectedItem(existing.getContext());
            if (existing.getShortcut() != null) shortcutField.setText(existing.getShortcut().toString());
        }
    }

    private void save() {
        String name = nameField.getText().trim();
        String cmd = cmdField.getText().trim();
        if (name.isEmpty() || cmd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Command required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = existing != null ? existing.getId() : UUID.randomUUID().toString();
        result = new Command(id, name, cmd, descField.getText().trim(),
            (CommandContext) ctx_combo.getSelectedItem(), KeyStroke.getKeyStroke(shortcutField.getText()));
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public Command getCommand() { return result; }
}
