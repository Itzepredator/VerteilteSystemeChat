package info.sperber.socket;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class ChatClientHibernate {

	@Id
	@GenerateValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String vorname;
	private String nachname;
	
	@OneToMany(mappedBy="owner", cascade=CascadeType.PERSIST)
	private List<MessageHibernate> messages = new ArrayList<MessageHibernate>();
}
