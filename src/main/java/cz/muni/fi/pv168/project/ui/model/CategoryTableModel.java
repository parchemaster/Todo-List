package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.data.CategoryDao;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Task;
import cz.muni.fi.pv168.project.ui.i18n.I18N;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryTableModel extends AbstractEntityTableModel<Category> implements EditableModel<Category> {

    private static final I18N I18N = new I18N(CategoryTableModel.class);

    private final List<Category> categories;
    private final CategoryDao categoryDao;
    private final List<Task> tasks;

    public CategoryTableModel(List<Category> categories, CategoryDao categoryDao, List<Task> tasks) {
        super(List.of(
                Column.readOnly(I18N.getString("name"), String.class, Category::getName),
                Column.readOnly(I18N.getString("spentTimeForWeek"), Integer.class, category -> category.findWeekHours(tasks)),
                Column.readOnly(I18N.getString("spentTimeForMonth"), Integer.class, category -> category.findMonthHours(tasks)),
                Column.readOnly(I18N.getString("spentTimeForYear"), Integer.class, category -> category.findYearHours(tasks))
        ));
        this.categories = new ArrayList<>(categories);
        this.categoryDao = categoryDao;
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public void deleteRow(int rowIndex) {
        categoryDao.delete(getEntity(rowIndex));
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public void addRow(Category category) {
        int newRowIndex = categories.size();
        categoryDao.create(category);
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(Category category) {
        int rowIndex = categories.indexOf(category);
        categoryDao.update(category);
        tasks.forEach(task -> {
            if (Objects.equals(task.getCategory().getId(), category.getId())) {
                task.getCategory().setName(category.getName());
            }
        });
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }
}
