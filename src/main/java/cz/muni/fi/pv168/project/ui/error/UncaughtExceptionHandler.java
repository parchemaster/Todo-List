package cz.muni.fi.pv168.project.ui.error;

import cz.muni.fi.pv168.project.ui.i18n.I18N;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final I18N I18N = new I18N(UncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ErrorDialog.show(I18N.getFormattedMessage("message", getMessage(e)), e);
    }

    private String getMessage(Throwable e) {
        return e.getClass().getSimpleName() + (e.getMessage() == null ? "" : ": " + e.getMessage());
    }

}
