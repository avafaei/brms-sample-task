package com.sample.task.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.task.Group;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.hornetq.HornetQTaskServer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;




public class StartUpServlet extends HttpServlet {

	private static final long serialVersionUID = 5L;
	
	private TaskServer server = null;
    private Thread thread = null;
    
    
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("In servlet init()");
		WebApplicationContext webCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		TaskService taskService = (TaskService) webCtx.getBean("taskService");
		System.out.println("In servlet init()");
		Map<String, User> users = new HashMap<String, User>();
        Map<String, Group> groups = new HashMap<String, Group>();
        users.put("Administrator",new User("Administrator"));
		users.put("makerAdmin",new User("makerAdmin"));
		users.put("maker1",new User("maker1"));
	    users.put("nameScreenerAdmin", new User("nameScreenerAdmin"));
		taskService.addUsersAndGroups(users, groups);
        server = new HornetQTaskServer(taskService, 5446);
		
		thread = new Thread(server);
		thread.start();
		System.out.println("Waiting for the HornetQTask Server to come up");
        while (!server.isRunning()) {

        	try {
				Thread.sleep( 50 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		System.out.println("HornetQ Task service started correctly !");
        System.out.println("HornetQ Task service running ...");
		
	}
    
    
    @Override
	public void destroy() {

		try {
			this.server.stop();
		} catch (Exception e) {
			System.out.println("Exception while stopping task server " + e.getMessage());
		}
		try {
			 this.thread.interrupt();
		} catch (Exception e) {
			System.out.println("Exception while stopping task server thread " + e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);

    }

	protected void doPost(HttpServletRequest reqquest,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(1001, "POST Method Not Allowed Here");
	}

	

}
