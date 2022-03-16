package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ActionBack extends AbstractAction {

    private static final I18N I18N = new I18N(ActionBack.class);

    private final Component component;
    private final JTabbedPane tabbedPane;
    private final JList<?> listOriginal;
    private final JPanel leftPanelSubtask;
    private final JToolBar toolBar;

    public ActionBack(Component component, JTabbedPane tabbedPane, JList<?> listOriginal, JPanel leftPanelSubtask, JToolBar toolBar) {
        super(I18N.getString("back"));
        this.component = component;
        this.tabbedPane = tabbedPane;
        this.listOriginal = listOriginal;
        this.leftPanelSubtask = leftPanelSubtask;
        this.toolBar = toolBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tabbedPane.setComponentAt(0, component);
        leftPanelSubtask.remove(0);
        leftPanelSubtask.add(listOriginal);
        toolBar.setVisible(true);
        tabbedPane.updateUI();
    }
}
