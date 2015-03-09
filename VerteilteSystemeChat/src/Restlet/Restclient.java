package Restlet;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Restclient {
	public static void main(String[] args) {
		World world = new World("Der Client ist toll");

		Representation rep = new JacksonRepresentation<World>(world);
		new ClientResource("http://127.0.0.1:8081/rest/hello/10").put(rep);
	}
}
