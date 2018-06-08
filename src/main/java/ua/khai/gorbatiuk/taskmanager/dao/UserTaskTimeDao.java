package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.bean.UserTaskTime;

public interface UserTaskTimeDao {
    UserTaskTime getStartTimeByUserId(Integer userId);
    int setStartTime(UserTaskTime userTaskTime);
    int deleteStartTime(UserTaskTime userTaskTime);
}
