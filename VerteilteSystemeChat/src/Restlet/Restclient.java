package Restlet;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Restclient {
	public static void main(String[] args) {

        Restclient.sendMessage();
        Restclient.recieveMessage();

	}
    private static void sendMessage(){
        Message message = new Message("Der Client ist toll");

        Representation rep = new JacksonRepresentation<Message>(message);
        new ClientResource("http://127.0.0.1:8081/rest/hello/10").put(rep);
    }

    private static void recieveMessage(){

    }
}
