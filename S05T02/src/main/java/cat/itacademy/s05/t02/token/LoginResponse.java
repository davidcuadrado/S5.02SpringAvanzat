package cat.itacademy.s05.t02.token;

public record LoginResponse(String token, String role) {
	
	public String token() {
		return token;
	}

	public String role() {
		return role;
	}

}
