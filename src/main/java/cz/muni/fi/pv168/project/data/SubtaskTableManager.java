package cz.muni.fi.pv168.project.data;

public class SubtaskTableManager{

    public static void initTable(SubtaskDao dao) {
        if (!dao.tableExists()) {
            dao.createTable();
        }
    }
}
