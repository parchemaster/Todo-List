package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

public class CategoryDialogFactory implements EntityDialogFactory<Category> {

    private static final I18N I18N = new I18N(CategoryDialogFactory.class);

    @Override
    public EntityDialog<Category> newEditDialog(Category category) {
        return new CategoryDialog(category, I18N.getString("editCategory"));
    }

    @Override
    public EntityDialog<Category> newAddDialog() {
        var category = new Category(I18N.getString("newCategory"));
        return new CategoryDialog(category, I18N.getString("addCategory"));
    }
}
