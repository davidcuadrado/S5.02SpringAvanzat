package cat.itacademy.s05.t02.exceptions;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
        super(message);
    }
}