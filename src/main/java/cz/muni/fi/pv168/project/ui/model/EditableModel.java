package cz.muni.fi.pv168.project.ui.model;

public interface EditableModel<E> {

    void deleteRow(int rowIndex);

    void addRow(E entity);

    void updateRow(E entity);

    E getEntity(int rowIndex);
}
