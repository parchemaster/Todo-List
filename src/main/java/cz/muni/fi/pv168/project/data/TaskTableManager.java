package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.demodata.DemoDataReader;
import cz.muni.fi.pv168.project.model.Category;

import java.io.IOException;
import java.util.function.Function;

public class TaskTableManager{

    public static void initTable(TaskDao dao, Function<String, Category> categoryResolverByName) throws IOException {
        if (!dao.tableExists()) {
            dao.createTable();
            DemoDataReader.getTasks().forEach(t -> {
                t.setCategory(categoryResolverByName.apply(t.getCategory().getName()));
                dao.create(t);
            });
        }
    }
}
