package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;

final public class CategoryDialog extends EntityDialog<Category> {

    private static final I18N I18N = new I18N(CategoryDialog.class);

    private final JTextField categoryName = new JTextField(20);

    private final Category category;

    public CategoryDialog(Category category, String title) {
        super(title);
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        categoryName.setText(category.getName());
    }

    private void addFields() {
        add(I18N.getString("name"), categoryName);
    }

    @Override
    Category getEntity() {
        category.setName(categoryName.getText());
        return category;
    }
}

