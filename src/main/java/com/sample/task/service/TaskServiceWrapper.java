package com.sample.task.service;

import org.jbpm.task.TaskService;

public interface TaskServiceWrapper {
    TaskService getTaskService();
    void setTaskServiceImpl(org.jbpm.task.service.TaskService taskServiceImpl);
}
