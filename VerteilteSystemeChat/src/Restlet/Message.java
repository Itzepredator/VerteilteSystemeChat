package Restlet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("password")
public class Message {
	public String message;
	public String password = "superSecret";

	// public Message(){
	//
	// }
	public Message(String string) {
		this.message = string;
	}
}