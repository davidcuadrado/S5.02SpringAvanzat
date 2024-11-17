package cat.itacademy.s05.t02.token;

public record LoginResponse(String jwtToken, String userRole) {
	
	public String jwtToken() {
		return jwtToken;
	}

	public String userRole() {
		return userRole;
	}

}
