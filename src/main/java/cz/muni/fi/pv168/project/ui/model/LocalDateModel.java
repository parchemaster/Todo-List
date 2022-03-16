package cz.muni.fi.pv168.project.ui.model;

import org.jdatepicker.AbstractDateModel;
import org.jdatepicker.DateModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LocalDateModel extends AbstractDateModel<LocalDateTime> implements DateModel<LocalDateTime> {

    @Override
    protected Calendar toCalendar(LocalDateTime from) {
        return GregorianCalendar.from(from.toLocalDate().atStartOfDay(ZoneId.systemDefault()));
    }

    @Override
    protected LocalDateTime fromCalendar(Calendar from) {
        return from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
