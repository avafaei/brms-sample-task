package com.sample.task.service;

import org.jbpm.task.service.TaskClient;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.runtime.StatefulKnowledgeSession;

public final class BrmsSession {
  
  private StatefulKnowledgeSession session ;
  private TaskClient client;
  private KnowledgeRuntimeLogger logger;
   
   
   public BrmsSession(StatefulKnowledgeSession session, TaskClient client,
		KnowledgeRuntimeLogger logger) {
	super();
	this.session = session;
	this.client = client;
	this.logger = logger;
}

   public void dispose() {
	  logger.close();
	  try {
		client.disconnect();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	session.dispose();
	
	  
   }
   public StatefulKnowledgeSession getSession() {
		return session;
   }
   
   public TaskClient getClient() {
		return client;
   }
   
   public KnowledgeRuntimeLogger getLogger() {
		return logger;
   }
}
