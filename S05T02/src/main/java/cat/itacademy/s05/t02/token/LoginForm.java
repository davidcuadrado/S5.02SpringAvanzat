package cat.itacademy.s05.t02.token;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
	    @NotBlank(message = "Username is required") String username,
	    @NotBlank(message = "Password is required") String password
	) {

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}
	
	
}
