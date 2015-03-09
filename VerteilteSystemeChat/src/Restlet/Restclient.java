package Restlet;

import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

public class Restclient {
	public static void main(String[] args) {
		Restclient.sendMessage();
		Restclient.recieveMessage();
	}

	private static void sendMessage() {

		Message message = new Message("Der Client ist toll");
		StringRepresentation rep = new StringRepresentation(message.message);
		new ClientResource("http://localhost:8081/restlet/test").put(rep);
	}

	private static void recieveMessage() {
	}
}
