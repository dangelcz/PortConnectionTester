package launch.exceptions;

public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ValidationException(String errorMessage)
	{
		super(errorMessage);
	}
}
