package me.kerkstra.jesseract;

import org.apache.commons.exec.*;
import java.io.IOException;


public class Jesseract 
{
	
	
	
	//wrapper for single document
	public boolean convertSingleDocument(String sourceFilename, String destinationFilename) throws TesseractException
	{
		boolean successFlag = false;
		int exitValue = sendTesseractCommand(sourceFilename, destinationFilename);
		
		if(exitValue == 0)
		{
			successFlag = true;
		}
		
		return successFlag;
	}
	
	//wrapper for reading all files in a specified directory
	//then fires off tesseract for each one.
	public boolean convertBatchDocuments(String fileDirectory)
	{
		boolean successFlag = false;
		
		//do work.
		
		
		return successFlag;
		
	}
	
	


	private int sendTesseractCommand(String sourceFilename, String destinationFilename) throws TesseractException
	{
		int exitValue = 1;

		try
		{
			String command = "tesseract " + sourceFilename + " " + destinationFilename;
			CommandLine cmdLine = CommandLine.parse(command);
			DefaultExecutor executor = new DefaultExecutor();
			ExecuteWatchdog watchdog = new ExecuteWatchdog(300000);
			executor.setWatchdog(watchdog);
			exitValue = executor.execute(cmdLine);
		}
		catch(ExecuteException ex)
		{	
			throw new TesseractException(ex.toString(), ex.getCause());
		}
		catch(IOException ioex)
		{
			throw new TesseractException(ioex.toString(), ioex.getCause());
		}
		catch(Exception genericException)
		{
			throw new TesseractException(genericException.toString(), genericException.getCause());
		}

		return exitValue;
	}
}
