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
	private String userId;
	@Getter
	@Setter
	private String username;
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	private ArrayList<Pet> petList;
	@Getter
	@Setter
	private String role;

	public MyUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

}