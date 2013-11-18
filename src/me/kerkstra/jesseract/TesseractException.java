package me.kerkstra.jesseract;

public class TesseractException extends Exception
{
	private static final long serialVersionUID = 8017151850190179784L;

	public TesseractException()
	{
		super();
	}
	
	public TesseractException(String message)
	{
		super(message);
	}

	public TesseractException(String message, Throwable cause) 
	{
        super(message, cause);
    }
	
	public TesseractException(Throwable cause)
	{
		super(cause);
	}
	
}
