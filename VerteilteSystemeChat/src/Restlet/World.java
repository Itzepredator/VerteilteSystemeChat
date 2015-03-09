package Restlet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("password")
public class World {
	
	public String message;
	
	public String password ="superSecret";
	public World(){
		
	}
	
	public World(String string){
		this.message=string;
	}

}
