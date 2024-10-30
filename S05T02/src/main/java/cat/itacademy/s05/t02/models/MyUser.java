package cat.itacademy.s05.t02.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "User entity that represent every user created. ")
@Document(collection = "user")
public class MyUser {

	@Id
	private @Setter @Getter String userId;
	private @Setter @Getter String username;
	private @Setter @Getter String password;
	private @Setter @Getter String role;
	private @Setter @Getter ArrayList<Pet> petList;


	public MyUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return this.role;
	}

	public void setPassword(String password) {
		this.password = password;
		
	}

	

}