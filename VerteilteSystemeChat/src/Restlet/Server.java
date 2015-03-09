package Restlet;

import java.awt.List;
import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

public class Server extends ServerResource {
	public List list = new List();

	public static void main(final String[] args) throws Exception {
		// Create a new Component.
		final Component component = new Component();

		// Add a new HTTP server listening on port 8081.
		component.getServers().add(Protocol.HTTP, 8081);

		final Router router = new Router(component.getContext()
				.createChildContext());

		router.attach("/test", Server.class);

		// Attach the sample application.
		component.getDefaultHost().attach("/restlet", router);

		// Start the component.
		component.start();
	}

	// Server to Server passiv
	@Get
	public List messageToServer() {
		return list;

	}

	@Put
	public void messageFromClient(StringRepresentation rep) throws IOException {
		System.out.println("world has message " + rep);
	}

}