package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends AbstractAction {

    private static final I18N I18N = new I18N(QuitAction.class);

    public QuitAction() {
        super(I18N.getString("quit"), Icons.QUIT_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("terminate"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
