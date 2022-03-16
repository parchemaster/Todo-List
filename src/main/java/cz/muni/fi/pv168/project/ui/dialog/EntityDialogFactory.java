package cz.muni.fi.pv168.project.ui.dialog;

public interface EntityDialogFactory<E> {

    EntityDialog<E> newEditDialog(E entity);

    EntityDialog<E> newAddDialog();
}
