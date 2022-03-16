package cz.muni.fi.pv168.project.ui.model;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Column<E, T> {

    private final String columnName;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    private Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        this.columnName = Objects.requireNonNull(columnName, "columnName");
        this.columnClass = Objects.requireNonNull(columnClass, "columnClass");
        this.valueGetter = Objects.requireNonNull(valueGetter, "valueGetter");
    }

    static <E, T> Column<E, T> editable(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        return new Editable<>(columnName, columnClass, valueGetter, valueSetter);
    }

    static <E, T> Column<E, T> readOnly(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        return new ReadOnly<>(columnName, columnClass, valueGetter);
    }

    Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    abstract void setValue(Object value, E entity);

    String getColumnName() {
        return columnName;
    }

    Class<T> getColumnClass() {
        return columnClass;
    }

    abstract boolean isEditable();

    private static class ReadOnly<E, T> extends Column<E, T> {

        private ReadOnly(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
            super(columnName, columnClass, valueGetter);
        }

        @Override
        boolean isEditable() {
            return false;
        }

        @Override
        void setValue(Object value, E entity) {
            throw new UnsupportedOperationException("Column '" + getColumnName() + "' is not editable");
        }
    }

    private static class Editable<E, T> extends Column<E, T> {

        private final BiConsumer<E, T> valueSetter;

        private Editable(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
            super(columnName, columnClass, valueGetter);
            this.valueSetter = Objects.requireNonNull(valueSetter, "valueSetter");
        }

        @Override
        boolean isEditable() {
            return true;
        }

        @Override
        void setValue(Object value, E entity) {
            valueSetter.accept(entity, getColumnClass().cast(value));
        }
    }
}
