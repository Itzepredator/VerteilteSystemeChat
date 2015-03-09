package Restlet;

import org.json.JSONObject;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Restclient {
	public static void main(String[] args) {
		Restclient.sendMessage();
		Restclient.recieveMessage();
	}

	private static void sendMessage() {

		Message message = new Message("Der Client ist toll");
		JSONObject jb = new JSONObject(message);
		Representation rep = new JacksonRepresentation(jb);
		new ClientResource("http://localhost:8081/restlet/test").put(rep);
	}

	private static void recieveMessage() {
	}
}
