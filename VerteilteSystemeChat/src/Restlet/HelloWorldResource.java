package Restlet;

import java.io.IOException;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class HelloWorldResource extends ServerResource {
	@Get
	public Representation helloWorld() {
		Message message = new Message("halloJSON Welt");
		return new JacksonRepresentation<Message>(message);
	}

	@Put
	public void update(Representation rep) throws IOException {
		if (getRequestAttributes().containsKey("id")) {
			System.out.println("id is " + getRequestAttributes().get("id"));
		}
		JacksonRepresentation<Message> wr = new JacksonRepresentation<Message>(
				rep, Message.class);
		Message w = wr.getObject();
		System.out.println("world has message " + w.message);
	}
}