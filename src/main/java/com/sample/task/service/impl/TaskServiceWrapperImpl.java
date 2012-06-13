package com.sample.task.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.task.Group;
import org.jbpm.task.TaskService;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;

import com.sample.task.service.TaskServiceWrapper;



public class TaskServiceWrapperImpl implements TaskServiceWrapper{
	
	private TaskService taskService;

	public TaskService getTaskService() {
		return taskService;
	}
	
	

	public void setTaskServiceImpl(
			org.jbpm.task.service.TaskService taskServiceImpl) {
		Map<String, User> users = new HashMap<String, User>();
        Map<String, Group> groups = new HashMap<String, Group>();
        users.put("Administrator",new User("Administrator"));
		users.put("makerAdmin",new User("makerAdmin"));
	    users.put("nameScreenerAdmin", new User("nameScreenerAdmin"));
		taskServiceImpl.addUsersAndGroups(users, groups);
		this.taskService = new LocalTaskService(taskServiceImpl);
	}
	
}
