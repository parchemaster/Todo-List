package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.demodata.DemoDataReader;

import java.io.IOException;

public class CategoryTableManager{

    public static void initTable(CategoryDao dao) throws IOException {
        if (!dao.tableExists()) {
            dao.createTable();
            DemoDataReader.getCategories().forEach(c -> {
                if (dao.findByName(c.getName()) == null)
                    dao.create(c);
            });
        }
    }

}
