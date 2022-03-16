package cz.muni.fi.pv168.project.ui.renderer;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocalDateTimeRenderer  extends AbstractRenderer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd LLLL yyyy");

    public LocalDateTimeRenderer() {
        super(LocalDateTime.class);
    }

    @Override
    protected void updateLabel(JLabel label, LocalDateTime value) {
        label.setText(formatDate(value));
    }

    public static String formatDate(LocalDateTime value) {
        return formatter.format(value);
    }

}