package cz.muni.fi.pv168.project.ui.renderer;

import cz.muni.fi.pv168.project.model.TaskState;
import cz.muni.fi.pv168.project.ui.i18n.I18N;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.util.Map;

public class TaskStateRenderer extends AbstractRenderer<TaskState> {

    private static final I18N I18N = new I18N(TaskStateRenderer.class);

    public TaskStateRenderer() {
        super(TaskState.class);
    }

    Map<TaskState, Icon> iconMap = Map.of(
            TaskState.WAITING, Icons.WAITING_ICON,
            TaskState.IN_PROGRESS, Icons.IN_PROGRESS_ICON,
            TaskState.FINISHED, Icons.FINISHED_ICON
    );

    @Override
    protected void updateLabel(JLabel label, TaskState value) {
        label.setText(I18N.getString(value.toString()));
        label.setIcon(iconMap.get(value));
    }
}