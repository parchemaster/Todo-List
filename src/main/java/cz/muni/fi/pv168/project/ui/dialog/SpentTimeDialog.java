package cz.muni.fi.pv168.project.ui.dialog;


import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;

public class SpentTimeDialog extends EntityDialog {

    private static final I18N I18N = new I18N(SpentTimeDialog.class);

    private final JSpinner spentTime = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    public SpentTimeDialog() {
        super(I18N.getString("spentTimeTitle"));
        addFields();
    }


    private void addFields() {
        add(I18N.getString("spentTime"), spentTime);
    }


    public int getTime() {
        return  Integer.parseInt(spentTime.getValue().toString());
    }


    @Override
    Long getEntity() {
        return Long.parseLong(spentTime.getValue().toString());
    }
}
