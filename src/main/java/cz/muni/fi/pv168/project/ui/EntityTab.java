package cz.muni.fi.pv168.project.ui;

//import cz.muni.fi.pv168.project.ui.model.EditableModel;

import cz.muni.fi.pv168.project.ui.dialog.EntityDialogFactory;
import cz.muni.fi.pv168.project.ui.model.EditableModel;

import javax.swing.*;

public interface EntityTab<E> {

    EditableModel<E> getEditableModel();

    EntityDialogFactory<E> getEntityDialogFactory();

    int getSelectedRowsCount();

    JTable getTable();
}
