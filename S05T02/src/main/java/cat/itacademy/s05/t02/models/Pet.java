package cat.itacademy.s05.t02.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "Pet entity that represent every existing pet. ")
@Document(collection = "pet")
public class Pet {

	@Id
	private String id;
	@Getter
	@Setter
	private String userId;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private PetType petType;
	@Getter
	@Setter
	private PetMood currentMood;
	@Getter
	@Setter
	private int happiness;
	@Getter
	@Setter
	private int energy;
	@Getter
	@Setter
	private int hunger;
	@Getter
	@Setter
	private String color;
	@Getter
	@Setter
	private ArrayList<String> specialTreats;
	

	public Pet(String name, String userId) {
		this.name = name;
		this.userId = userId;
	}


	public String getPetId() {
		return this.id;
	}
	
	public String getUserId() {
		return this.userId;
	}


	public void setName(String name2) {
		// TODO Auto-generated method stub
		
	}


	public void setOwnerId(String userId2) {
		// TODO Auto-generated method stub
		
	}

}