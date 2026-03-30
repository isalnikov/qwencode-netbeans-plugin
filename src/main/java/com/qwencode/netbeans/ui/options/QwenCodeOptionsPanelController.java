package com.qwencode.netbeans.ui.options;

import com.qwencode.netbeans.core.CommandManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@OptionsPanelController.SubRegistration(
    displayName = "#OptionsDialog_Name",
    keywords = "#OptionsDialog_Keywords",
    keywordsCategory = "QwenCode"
)
@NbBundle.Messages({
    "OptionsDialog_Name=QwenCode",
    "OptionsDialog_Keywords=qwen,code,ai,commands,shortcuts"
})
public class QwenCodeOptionsPanelController extends OptionsPanelController {
    private QwenCodeOptionsPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;
    private final CommandManager cmdMgr = new CommandManager();

    public QwenCodeOptionsPanelController() { cmdMgr.loadCommands(); }

    @Override public void update() { if (panel != null) panel.load(); }
    @Override public void applyChanges() { if (panel != null) { panel.store(); cmdMgr.saveCommands(); } }
    @Override public void cancel() {}
    @Override public boolean isValid() { return true; }
    @Override public boolean isChanged() { return changed; }
    @Override public void addPropertyChangeListener(PropertyChangeListener l) { pcs.addPropertyChangeListener(l); }
    @Override public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }
    @Override public JComponent getComponent(Lookup l) {
        if (panel == null) panel = new QwenCodeOptionsPanel(cmdMgr, this);
        return panel;
    }
    @Override public HelpCtx getHelpCtx() { return new HelpCtx("QwenCodeOptionsPanel"); }

    public void fireChanged() {
        if (!changed) { changed = true; pcs.firePropertyChange(PROP_CHANGED, false, true); }
    }
}
