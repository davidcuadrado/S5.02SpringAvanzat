package cat.itacademy.s05.t02.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User entity that represent every user created. ")
@Document(collection = "user")
public class MyUser {

	@Id
	private String userId;
	private String username;
	private String password;
	private String role;

	public MyUser(String username, String password) {
		this.username = username;
		this.password = password;
		this.role = "USER";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}