package cat.itacademy.s05.t02.exceptions;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
        super(message);
    }
}