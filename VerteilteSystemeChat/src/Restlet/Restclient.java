package Restlet;

import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Put;

public class Restclient {
	public static void main(String[] args) {
		Restclient.sendMessage("Deine Muddah");
		//Restclient.recieveMessage();
	}

	private static void sendMessage(String string) {

		Message message = new Message(string);
		StringRepresentation rep = new StringRepresentation("1"+message.message);
		new ClientResource("http://localhost:8081/restlet/test").put(rep);
	}

	//Server-Client-Put
	@Put
	private static void recieveMessage(StringRepresentation serverMessage) {
		System.out.println(serverMessage);
	}
}
