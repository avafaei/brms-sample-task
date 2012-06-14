package com.sample.brms.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jbpm.task.AccessType;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sample.task.service.BrmsFactory;
import com.sample.task.service.BrmsSession;


@Controller
@RequestMapping(value="/search")
public class SearchController {


	@Autowired
	private BrmsFactory brmsFactory;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute(new Search());
		return "search/searchForm";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid Search search, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "search/searchForm";
		}
	    BrmsSession brmsSession = brmsFactory.getBrmsSession();
    	Map<String, Object> params = new HashMap<String, Object>();	
		params.put("decision", "search");
		params.put("ssn", search.getSsn());
		params.put("lastName", search.getLastName());
		params.put("firstName", search.getFirstName());
		params.put("userId", "nameScreenerAdmin");
		brmsSession.getSession().startProcess("com.citi.kyc.addNewClient", params);
		brmsSession.dispose();
		return "redirect:/search/view";
		
	}
	
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public String getView() {
		BrmsSession brmsSession = brmsFactory.getBrmsSession();
		BlockingTaskSummaryResponseHandler taskSummaryResponseHandler = new BlockingTaskSummaryResponseHandler();
		BlockingTaskOperationResponseHandler taskOperationResponseHandler = new BlockingTaskOperationResponseHandler();
		brmsSession.getClient().getTasksAssignedAsPotentialOwner("nameScreenerAdmin", "en-UK",
				taskSummaryResponseHandler);
		List<TaskSummary> list = taskSummaryResponseHandler.getResults();
		TaskSummary task = list.get(0);
		brmsSession.getClient().start(task.getId(), "nameScreenerAdmin", taskOperationResponseHandler);
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("decision", "nameScreening");
		completeTask(task.getId(), "nameScreenerAdmin", results,
				taskOperationResponseHandler, brmsSession.getClient());
		brmsSession.dispose();
		return "search/view";
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
