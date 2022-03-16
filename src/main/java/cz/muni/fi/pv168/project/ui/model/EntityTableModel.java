package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.TableModel;

public interface EntityTableModel<E> extends TableModel {

    E getEntity(int rowIndex);
}
