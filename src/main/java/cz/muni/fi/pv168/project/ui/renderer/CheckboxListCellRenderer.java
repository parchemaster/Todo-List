package cz.muni.fi.pv168.project.ui.renderer;

import cz.muni.fi.pv168.project.model.Subtask;

import javax.swing.*;
import java.awt.*;

public class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<Subtask> {

    public Component getListCellRendererComponent(JList<? extends Subtask> list, Subtask value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(value.isDone());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}