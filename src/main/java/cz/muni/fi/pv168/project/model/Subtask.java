package cz.muni.fi.pv168.project.model;

import java.util.Objects;

public class Subtask {

    private Long id;
    private String name;
    private boolean done = false;
    private Task task;

    public Subtask(String name, boolean done, Task task) {
        this.name = name;
        this.done = done;
        this.task = task;
    }

    public Subtask() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(id, subtask.id);
    }
}