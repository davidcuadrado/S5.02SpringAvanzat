package cat.itacademy.s05.t02.token;

public record LoginForm (String username, String password) {

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}
	
	
}
