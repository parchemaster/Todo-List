package cz.muni.fi.pv168.project.ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public abstract class EntityDialog<E> {

    private final JPanel panel = new JPanel();
    private int nextComponentRow = 0;
    private final String title;
    private final String text;
    private final int option;
    private final boolean infoBox;

    EntityDialog(String title) {
        this.title = title;
        panel.setLayout(new GridBagLayout());
        text = null;
        option = -1;
        infoBox = false;
    }

    EntityDialog(String title, String text, int optionPane) {
        this.title = title;
        this.text = text;
        option = optionPane;
        infoBox = true;
    }

    void add(String labelText, JComponent component) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0.0;
        var label = new JLabel(labelText);
        label.setLabelFor(component);
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        panel.add(component, c);
    }

    abstract E getEntity();

    public Optional<E> show(JComponent parentComponent) {
        if (!infoBox) {
            int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                return Optional.of(getEntity());
            } else {
                return Optional.empty();
            }
        } else {
            JOptionPane.showMessageDialog(parentComponent, text, title, option);
            return Optional.empty();
        }
    }
}
