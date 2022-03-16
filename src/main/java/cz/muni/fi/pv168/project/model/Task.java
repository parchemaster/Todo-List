package cz.muni.fi.pv168.project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Task {

    private Long id;
    private String taskName;
    private String location;
    private LocalDateTime deadline;
    private LocalDateTime startTime;
    private LocalDateTime doneTime;
    private int estimatedTime;
    private Category category;
    private int progress = 0;
    private TaskState taskState = TaskState.WAITING;
    private int spentTime = 0;

    public Task(String taskName, String location, LocalDateTime deadline, LocalDateTime startTime, int estimatedTime, Category category) {
        this.taskName = taskName;
        this.location = location;
        this.deadline = deadline;
        this.startTime = startTime;
        this.doneTime = LocalDateTime.of(2099, 10, 25, 13, 49, 0);
        this.estimatedTime = estimatedTime;
        this.category = category;
    }

    public Task() {
    }

    public LocalDateTime getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(LocalDateTime doneTime) {
        this.doneTime = doneTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getProgress() {
        return progress;
    }

    public int getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(int spentTime) {
        this.spentTime = spentTime;
    }

    public void setProgress(List<Subtask> subtaskList) {
        float count = 0;
        for(Subtask subTask : subtaskList)
            if (subTask.isDone())
                count++;
        this.progress = Math.round(count / subtaskList.size() * 100);
        if (this.progress > 0) setStateOfTask(TaskState.IN_PROGRESS);
        if (this.progress == 100) setStateOfTask(TaskState.FINISHED);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public TaskState getStateOfTask() {
        return taskState;
    }

    public void setStateOfTask(TaskState taskState) {
        this.taskState = taskState;
    }

    public Category getCategory() {
        return category;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

}