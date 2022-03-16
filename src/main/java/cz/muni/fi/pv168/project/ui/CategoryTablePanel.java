package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.model.EditableModel;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialogFactory;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialogFactory;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;


import javax.swing.*;
import java.awt.*;

public class CategoryTablePanel extends JPanel implements EntityTab<Category> {

    private final JTable categoryTable;
    private final CategoryTableModel categoryTableModel;
    private final CategoryDialogFactory dialogFactory;

    public CategoryTablePanel(CategoryTableModel categoryTableModel) {
        this.categoryTableModel = categoryTableModel;
        this.categoryTable = createCategoryTable(categoryTableModel);
        dialogFactory = new CategoryDialogFactory();
        setLayout(new BorderLayout());
        add(new JScrollPane(categoryTable));
    }

    private JTable createCategoryTable(CategoryTableModel tableModel) {
        var table = new JTable(tableModel);
        table.setRowHeight(25);
        return table;
    }

    @Override
    public EditableModel<Category> getEditableModel() {
        return categoryTableModel;
    }

    @Override
    public EntityDialogFactory<Category> getEntityDialogFactory() {
        return dialogFactory;
    }

    @Override
    public int getSelectedRowsCount() {
        return categoryTable.getSelectedRowCount();
    }

    public JTable getTable() {
        return categoryTable;
    }
}

