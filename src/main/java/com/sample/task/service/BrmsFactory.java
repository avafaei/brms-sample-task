package com.sample.task.service;

import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListenerFactory;
import org.drools.impl.EnvironmentFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.hornetq.CommandBasedHornetQWSHumanTaskHandler;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

public class BrmsFactory {
	
	
	private KnowledgeBase kbase;
	
	
	private EntityManagerFactory htEmf;
	
	
	private JpaTransactionManager jpaHtTxMgr;
	
	public KnowledgeBase getKbase() {
		return kbase;
	}


	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}


	public EntityManagerFactory getHtEmf() {
		return htEmf;
	}

	
	public void setHtEmf(EntityManagerFactory htEmf) {
		this.htEmf = htEmf;
	}


	public JpaTransactionManager getJpaHtTxMgr() {
		return jpaHtTxMgr;
	}


	public void setJpaHtTxMgr(JpaTransactionManager jpaHtTxMgr) {
		this.jpaHtTxMgr = jpaHtTxMgr;
	}
	
	
	public BrmsSession getBrmsSession() {
		Environment env = EnvironmentFactory.newEnvironment();
        if (null == kbase){ 
        	System.out.println("Kbase not set!");
        }
        if (null == htEmf){ 
        	System.out.println("EMF not set!");
        }
        if (null == jpaHtTxMgr){ 
        	System.out.println("jpaHtTxMgr not set!");
        }
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, htEmf);
        env.set(EnvironmentName.TRANSACTION_MANAGER, jpaHtTxMgr);
        StatefulKnowledgeSession ksession = JPAKnowledgeService.newStatefulKnowledgeSession( kbase, null, env );
        CommandBasedHornetQWSHumanTaskHandler taskHandler = new CommandBasedHornetQWSHumanTaskHandler(ksession);
 		HornetQTaskClientHandler handler = new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener());
 		TaskClient client = new TaskClient(new HornetQTaskClientConnector("client1", handler));
 		handler.setClient(client);
 	    client.connect("127.0.0.1", 5446);
 		taskHandler.setClient(client);
 	    ksession.getWorkItemManager().registerWorkItemHandler("Human Task", taskHandler);	
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "audit");
		BrmsSession brmsSession = new BrmsSession(ksession, client, logger);
		return brmsSession;
		 
	}



	
	
}
