package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Subtask;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;

public class AddSubtaskDialog extends EntityDialog<Subtask> {

    private static final I18N I18N = new I18N(AddSubtaskDialog.class);

    private final JTextField subtaskName = new JTextField(20);
    private final Subtask exampleItem;
    private final Subtask resultItem;

    public AddSubtaskDialog(Subtask item) {
        super(I18N.getString("title"));
        this.exampleItem = item;
        this.resultItem = new Subtask();
        setValues();
        addFields();
    }

    private void setValues() {
        subtaskName.setText(exampleItem.getName());
    }

    private void addFields() {
        add(I18N.getString("name"), subtaskName);
    }

    @Override
    Subtask getEntity() {
        resultItem.setName(subtaskName.getText());
        resultItem.setTask(exampleItem.getTask());
        return resultItem;
    }
}
