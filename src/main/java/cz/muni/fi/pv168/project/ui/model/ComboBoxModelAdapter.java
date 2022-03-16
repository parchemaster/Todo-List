package cz.muni.fi.pv168.project.ui.model;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.Objects;

public class ComboBoxModelAdapter<E> extends AbstractListModel<E> implements ComboBoxModel<E> {

    private final ListModel<E> listModel;
    private Object selectedItem;

    public ComboBoxModelAdapter(ListModel<E> listModel) {
        this.listModel = listModel;
    }

    @Override
    public int getSize() {
        return listModel.getSize();
    }

    @Override
    public E getElementAt(int index) {
        return listModel.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener listener) {
        super.addListDataListener(listener);
        listModel.addListDataListener(listener);
    }

    @Override
    public void removeListDataListener(ListDataListener listener) {
        super.removeListDataListener(listener);
        listModel.removeListDataListener(listener);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(Object item) {
        if (!Objects.equals(item, selectedItem)) {
            selectedItem = item;
            fireContentsChanged(this, -1, -1);
        }
    }
}
