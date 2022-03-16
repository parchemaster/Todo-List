package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.*;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("Crystal_Clear_action_button_cancel.png");
    public static final Icon EDIT_ICON = createIcon("Crystal_Clear_action_edit.png");
    public static final Icon ADD_ICON = createIcon("Crystal_Clear_action_edit_add.png");
    public static final Icon QUIT_ICON = createIcon("Crystal_Clear_action_exit.png");
    public static final Icon PROGRESS_ICON = createIcon("Eye.png");
    public static final Icon CATEGORY_ICON = createIcon("3dbarchart.png");
    public static final Icon LOCATION_ICON = createIcon("Earth.png");
    public static final Icon TASK_NAME_ICON = createIcon("View.png");
    public static final Icon START_DAY_ICON = createIcon("History.png");
    public static final Icon DEADLINE_ICON = createIcon("Schedule.png");
    public static final Icon ESTIMATED_TIME_ICON = createIcon("Stockgraph.png");
    public static final Icon WAITING_ICON = createIcon("Yellowtag.png");
    public static final Icon IN_PROGRESS_ICON = createIcon("Bluetag.png");
    public static final Icon FINISHED_ICON = createIcon("Greentag.png");
    public static final Icon TASK_ICON = createIcon("Notes.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
