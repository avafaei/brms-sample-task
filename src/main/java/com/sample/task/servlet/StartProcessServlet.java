package com.sample.task.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.SystemEventListenerFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils; 
import org.jbpm.process.workitem.wsht.SyncWSHumanTaskHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.hornetq.CommandBasedHornetQWSHumanTaskHandler;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;
import org.jbpm.task.AccessType;
import org.jbpm.task.query.TaskSummary;

import com.sample.task.service.TaskServiceWrapper;


public class StartProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 5L;
	private TaskService taskService = null;

    


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        
		WebApplicationContext webCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    	StatefulKnowledgeSession ksession = (StatefulKnowledgeSession) webCtx.getBean("ksession");
	        
    	CommandBasedHornetQWSHumanTaskHandler taskHandler = new CommandBasedHornetQWSHumanTaskHandler(ksession);
 		HornetQTaskClientHandler handler = new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener());
 		TaskClient client = new TaskClient(new HornetQTaskClientConnector("client1", handler));
 		handler.setClient(client);
 		boolean isConnected = client.connect("127.0.0.1", 5446);
 		taskHandler.setClient(client);
 	    ksession.getWorkItemManager().registerWorkItemHandler("Human Task", taskHandler);	
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "audit");
    	Map<String, Object> params = new HashMap<String, Object>();	
		params.put("decision", "search");
		params.put("ssn", "123456789");
		params.put("lastName", "Smith");
		params.put("userId", "maker1");
		ksession.startProcess("com.citi.kyc.addNewClient", params);
		BlockingTaskSummaryResponseHandler taskSummaryResponseHandler = new BlockingTaskSummaryResponseHandler();
		BlockingTaskOperationResponseHandler taskOperationResponseHandler = new BlockingTaskOperationResponseHandler(); 
		client.getTasksAssignedAsPotentialOwner("makerAdmin", "en-UK", taskSummaryResponseHandler);
	    List<TaskSummary> list = taskSummaryResponseHandler.getResults();
		
		
      //  List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("makerAdmin", "en-UK");
        TaskSummary task = list.get(0);
        client.start(task.getId(), "makerAdmin",  taskOperationResponseHandler);
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("decision", "nameScreening");
        completeTask(task.getId(), "makerAdmin", results, taskOperationResponseHandler,client);
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
				"Transitional//EN\">\n" +
				"<HTML>\n" +
				"<HEAD><TITLE>Started Process</TITLE></HEAD>\n" +
				"<BODY>\n" +
				"<H1>Started Process</H1>\n" + 
				"<H1>" + results.toString() +
				"</H1></BODY></HTML>");

		out.flush();
		logger.close();
        ksession.dispose();
       

    }

	protected void doPost(HttpServletRequest reqquest,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(1001, "POST Method Not Allowed Here");
	}
    
	
	
	private void completeTask(long taskId, String userId, Map<String, Object> results, BlockingTaskOperationResponseHandler taskOperationResponseHandler, TaskClient client) {
		ContentData contentData = null;
        if (results != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(results);
                out.close();
                contentData = new ContentData();
                contentData.setContent(bos.toByteArray());
                contentData.setAccessType(AccessType.Inline);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        client.complete(taskId, userId, contentData, taskOperationResponseHandler );
	}
	

}
