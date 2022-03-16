package cz.muni.fi.pv168.project.ui.error;

import cz.muni.fi.pv168.project.ui.i18n.I18N;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog {

    private static final I18N I18N = new I18N(ErrorDialog.class);

    private final JPanel panel = new JPanel();

    private ErrorDialog(String message, Throwable throwable) {
        panel.setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        var messageLabel = new JLabel(message);
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD));
        panel.add(messageLabel, constraints);
        String stackTrace = getStackTrace(throwable);
        var stackTraceTextArea = new JTextArea(stackTrace, 10, 80);
        stackTraceTextArea.setEditable(false);
        panel.add(new JScrollPane(stackTraceTextArea), constraints);
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private void show(Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent,
                panel,
                I18N.getString("title"),
                JOptionPane.ERROR_MESSAGE);
    }

    public static void show(String message, Throwable throwable) {
        show(message, throwable, null);
    }

    public static void show(String message, Throwable throwable, Component parentComponent) {
        new ErrorDialog(message, throwable).show(parentComponent);
    }

}

