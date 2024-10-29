package cat.itacademy.s05.t02.exceptions;

public class NotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
        super(message);
    }

}