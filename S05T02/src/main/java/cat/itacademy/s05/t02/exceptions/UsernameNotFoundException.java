package cat.itacademy.s05.t02.exceptions;

public class UsernameNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UsernameNotFoundException(String message) {
        super(message);
    }

}