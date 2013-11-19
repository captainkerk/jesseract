package me.kerkstra.jesseract;

import org.apache.commons.exec.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

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
	//then moves them to a child directory called 'completed' in the parent directory specified.
	public boolean convertBatchDocuments(String fileDirectory) throws IOException, TesseractException
	{
		boolean successFlag = false;
		final String image = "image";

		Path completedFolderLocation = Paths.get(fileDirectory + "\\completed\\");
		Path fileDirectoryPath = Paths.get(fileDirectory);
		List<Path> resultSet = new ArrayList<>();
		
		if(!completedFolderLocation.toFile().exists())
		{
			Files.createDirectories(completedFolderLocation);
		}

		try(DirectoryStream<Path> stream = Files.newDirectoryStream(fileDirectoryPath))
		{	
			for(Path entry : stream)
			{
				if(!entry.toFile().isDirectory())
				{
					if(Files.probeContentType(entry).contains(image))
					{
						resultSet.add(entry);
					}
				}
			}
		}		
		
		for(Path fileToConvert : resultSet)
		{
			//execute the tesseract single file wrapper 
			int exitValue = sendTesseractCommand(fileToConvert.toAbsolutePath().toString(), fileToConvert.toAbsolutePath().toString());
			if(exitValue != 0)
			{
				throw new TesseractException("Tesseract reports it failed to read or write");
			}
			else
			{
				//move file to completed directory
				Files.move(fileToConvert.toAbsolutePath(), Paths.get(completedFolderLocation.toAbsolutePath().toString() + "\\" + fileToConvert.getFileName()));
				successFlag = true;
			}
		}
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
