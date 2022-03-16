package cz.muni.fi.pv168.project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Category {

    private Long id;
    private String name;
    private long weekHours;
    private long monthHours;
    private long yearHours;

    public Category(String name) {
        setName(name);
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must nt be null");
    }

    public String getName() {
        return name;
    }

    public long getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(long weekHours) {
        this.weekHours = weekHours;
    }

    public long getMonthHours() {
        return monthHours;
    }

    public void setMonthHours(long monthHours) {
        this.monthHours = monthHours;
    }

    public long getYearHours() {
        return yearHours;
    }

    public void setYearHours(long yearHours) {
        this.yearHours = yearHours;
    }


    public int findWeekHours(List<Task> taskList) {
        int sum = 0;
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        for (Task task : taskList) {
            if (getName().equals(task.getCategory().toString()) && task.getStateOfTask() == TaskState.FINISHED && task.getDoneTime().isAfter(weekStart)) {
                sum += task.getSpentTime();
            }
        }
        setWeekHours(sum);
        return sum;
    }

    public int findMonthHours(List<Task> taskList) {
        int sum = 0;
        LocalDateTime monthStart = LocalDateTime.now().minusDays(30);
        for (Task task : taskList) {
            if (getName().equals(task.getCategory().toString()) && task.getStateOfTask() == TaskState.FINISHED && task.getDoneTime().isAfter(monthStart)) {
                sum += task.getSpentTime();
            }
        }
        setMonthHours(sum);
        return sum;
    }

    public int findYearHours(List<Task> taskList) {
        int sum = 0;
        LocalDateTime yearStart = LocalDateTime.now().minusDays(360);
        for (Task task : taskList) {
            if (getName().equals(task.getCategory().toString()) && task.getStateOfTask() == TaskState.FINISHED && task.getDoneTime().isAfter(yearStart)) {
                sum += task.getSpentTime();
            }
        }
        setYearHours(sum);
        return sum;
    }

    @Override
    public String toString() {
        return name;
    }


}

