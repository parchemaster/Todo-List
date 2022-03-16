package cz.muni.fi.pv168.project.ui.model;

import javax.swing.*;
import javax.swing.event.TableModelEvent;

/**
 * This class provides {@link javax.swing.ListModel} backed by existing
 * {@link EntityTableModel}. It allows to show the entities managed by
 * given table model in {@link javax.swing.JList} or {@link javax.swing.JComboBox}.
 *
 * <p>All the updates in the underlying table model are propagated to this {@code ListModel}.
 * It is achieved by translating {@link TableModelEvent}s to
 * {@link javax.swing.event.ListDataEvent}s.</p>
 *
 * @param <E> the type of the elements of this model
 */
public class EntityListModelAdapter<E> extends AbstractListModel<E> {

    private final EntityTableModel<E> entityTableModel;

    public EntityListModelAdapter(AbstractEntityTableModel<E> entityTableModel) {
        this.entityTableModel = entityTableModel;
        entityTableModel.addTableModelListener(this::handleTableModelEvent);
    }

    private void handleTableModelEvent(TableModelEvent event) {
        switch (event.getType()) {
            case TableModelEvent.INSERT:
                this.fireIntervalAdded(this, event.getFirstRow(), event.getLastRow());
                break;
            case TableModelEvent.UPDATE:
                this.fireContentsChanged(this, event.getFirstRow(), event.getLastRow());
                break;
            case TableModelEvent.DELETE:
                this.fireIntervalRemoved(this, event.getFirstRow(), event.getLastRow());
                break;
        }
    }

    @Override
    public int getSize() {
        return entityTableModel.getRowCount();
    }

    @Override
    public E getElementAt(int index) {
        return entityTableModel.getEntity(index);
    }
}
