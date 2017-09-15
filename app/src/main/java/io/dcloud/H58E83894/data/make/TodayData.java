package io.dcloud.H58E83894.data.make;

/**
 * Created by fire on 2017/8/14  09:30.
 */

public class TodayData {
    private int code;
    private int taskNumber;
    private TodayTaskData todayTask;
    private int taskDays;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public TodayTaskData getTodayTask() {
        return todayTask;
    }

    public void setTodayTask(TodayTaskData todayTask) {
        this.todayTask = todayTask;
    }

    public int getTaskDays() {
        return taskDays;
    }

    public void setTaskDays(int taskDays) {
        this.taskDays = taskDays;
    }
}
