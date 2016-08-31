package co.becuz.exceptions;

public class UserExistsException extends Exception {
	private static final long serialVersionUID = 7542423855841657422L;

	public UserExistsException(String message) {
        super(message);
    }
}