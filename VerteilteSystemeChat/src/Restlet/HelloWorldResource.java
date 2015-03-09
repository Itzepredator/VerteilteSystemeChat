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
		World world = new World("halloJSON Welt");
		return new JacksonRepresentation<World>(world);
	}

	@Put
	public void update(Representation rep) throws IOException {
		if (getRequestAttributes().containsKey("id")) {
			System.out.println("id is " + getRequestAttributes().get("id"));
		}

		JacksonRepresentation<World> wr = new JacksonRepresentation<World>(rep,
				World.class);
		World w = wr.getObject();
		System.out.println("world has message " + w.message);
	}
}
