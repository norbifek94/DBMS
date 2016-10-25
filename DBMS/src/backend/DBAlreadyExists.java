package backend;

public class DBAlreadyExists extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DBAlreadyExists() {
		super();
	}

	public DBAlreadyExists(final String message) {
		super(message);
	}

	public DBAlreadyExists(final String message, final Throwable cause) {
		super(message, cause);
	}
}
