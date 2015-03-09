package info.sperber.socket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Message;

public class AppHibernate {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
//		log.info("starting persistance");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("emf");
		
		EntityManager em= emf.createEntityManager();
		
		ChatClientHibernate chatClient = new ChatClientHibernate();
		chatClient.setName("Max");
		chatClient.setCreated(new Date());
		
		List messages = em.createQuery("from Message where owner)
		
		MessageHibernate message = new MessageHibernate();
		message.setOwner(chatClient);
		message.setContent("Hello World");
		message.setCreated(new Date());
		
		
	}
	
}
