package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.model.EditableModel;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialogFactory;
import cz.muni.fi.pv168.project.ui.EntityTab;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.Optional;
import java.util.function.IntPredicate;

public abstract class AbstractEntityAction extends AbstractAction {

    private final IntPredicate enabledCondition;
    private final JTabbedPane tabbedPane;
    private final ListSelectionListener selectionListener = e -> updateEnabledStatus();
    private EntityTab<?> currentTab;

    public AbstractEntityAction(JTabbedPane tabbedPane, IntPredicate enabledCondition) {
        this.enabledCondition = enabledCondition;
        this.tabbedPane = tabbedPane;
        this.tabbedPane.addChangeListener(e -> updateSelectedTab());
        updateSelectedTab();
    }

    protected Optional<EntityTab<?>> getSelectedTab() {
        var selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof EntityTab) {
            return Optional.of((EntityTab<?>) selectedComponent);
        } else {
            return Optional.empty();
        }
    }

    private void updateSelectedTab() {
        if (currentTab != null) {
            currentTab.getTable().getSelectionModel().removeListSelectionListener(selectionListener);
        }
        currentTab = getSelectedTab().orElse(null);
        if (currentTab != null) {
            currentTab.getTable().getSelectionModel().addListSelectionListener(selectionListener);
        }
        updateEnabledStatus();
    }

    private void updateEnabledStatus() {
        var selectedTab = getSelectedTab();
        if (selectedTab.isPresent()) {
            setEnabled(enabledCondition.test(selectedTab.get().getSelectedRowsCount()));
        } else {
            setEnabled(false);
        }
    }

    protected JTable getTable() {
        return getSelectedTab().orElseThrow().getTable();
    }

    protected <E> EditableModel<E> getTableModel() {
        return (EditableModel<E>) getSelectedTab().get().getEditableModel();
    }

    protected <E> EntityDialogFactory<E> getDialogFactory() {
        return (EntityDialogFactory<E>) getSelectedTab().get().getEntityDialogFactory();
    }
}
