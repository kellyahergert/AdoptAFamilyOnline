package aaf.model;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileWriter {

	private OutputStreamWriter writer;
	
	public FileWriter(String fileName) throws IOException {
		super();
		
		FileOutputStream outStream = new FileOutputStream(fileName, false);
		writer = new OutputStreamWriter(outStream, "UTF-8");
	}
	
	public void writeToFile(String text) throws IOException{
		writer.append(text);
	}
	
	public void close() throws IOException{
		writer.close();
	}
	
	
}
