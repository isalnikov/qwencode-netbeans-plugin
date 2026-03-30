package com.qwencode.netbeans.ui.actions;

import com.qwencode.netbeans.core.*;
import com.qwencode.netbeans.model.Command;
import com.qwencode.netbeans.ui.output.OutputTopComponent;
import com.qwencode.netbeans.ui.util.Icons;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "QwenCode", id = "com.qwencode.netbeans.ui.actions.QwenCodeAction")
@ActionRegistration(displayName = "#CTL_QwenCodeAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 100),
    @ActionReference(path = "Toolbars/Build", position = 200)
})
@Messages("CTL_QwenCodeAction=QwenCode")
public class QwenCodeAction extends AbstractAction {
    private static final Logger LOG = Logger.getLogger(QwenCodeAction.class.getName());
    private final CommandManager cmdMgr = new CommandManager();

    public QwenCodeAction() {
        cmdMgr.loadCommands();
        putValue(Action.NAME, Bundle.CTL_QwenCodeAction());
        putValue(Action.SMALL_ICON, Icons.getQwenCodeIcon());
        putValue(Action.SHORT_DESCRIPTION, "Execute QwenCode command");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showCommandMenu(e);
    }

    private void showCommandMenu(ActionEvent e) {
        JPopupMenu menu = new JPopupMenu();
        for (Command cmd : cmdMgr.getCommands()) {
            if (cmd.isEnabled()) {
                JMenuItem item = new JMenuItem(cmd.getName());
                item.addActionListener(ev -> executeCommand(cmd));
                menu.add(item);
            }
        }
        menu.addSeparator();
        JMenuItem settings = new JMenuItem("Settings...");
        settings.addActionListener(ev -> openSettings());
        menu.add(settings);

        if (e.getSource() instanceof java.awt.Component) {
            java.awt.Component c = (java.awt.Component) e.getSource();
            menu.show(c, c.getWidth() / 2, c.getHeight() / 2);
        }
    }

    private void executeCommand(Command cmd) {
        LOG.log(Level.INFO, "Execute: {0}", cmd.getName());
        ProjectContext ctx = ProjectContext.fromCurrentState();
        OutputTopComponent out = (OutputTopComponent) org.openide.windows.WindowManager.getDefault()
            .findTopComponent("OutputTopComponent");
        if (out == null) {
            out = OutputTopComponent.getInstance();
        }
        final OutputTopComponent outFinal = out;
        if (outFinal != null) {
            outFinal.requestActive();
            outFinal.startCommand(cmd.getName(), ctx.getProjectPath());
        }
        cmdMgr.executeCommand(cmd, ctx, new OutputListener() {
            public void onOutput(String l) { if (outFinal != null) outFinal.appendConsole(l); }
            public void onError(String l) { if (outFinal != null) outFinal.appendError(l); }
            public void onCompleted(boolean s, long d) { if (outFinal != null) outFinal.completeCommand(s, d); }
        });
    }

    private void openSettings() {
        // Открыть Options Dialog на вкладке QwenCode
        // Используем стандартный механизм NetBeans
        org.openide.windows.IOProvider io = org.openide.windows.IOProvider.getDefault();
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(
                null, 
                "Go to Tools → Options → QwenCode to configure commands", 
                "QwenCode Settings", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
