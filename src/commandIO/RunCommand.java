package commandIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RunCommand {
	
	static public void main(String[] args) {
		try 
		{ 
		Process p=Runtime.getRuntime().exec("cmd /k dir c:\\ /p"); 
		System.out.println("Antes del wait");
//		p.waitFor();

		BufferedWriter output=new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		
		output.write(" \n");
		System.out.println("Despues del wait");
		BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line=reader.readLine(); 
		
		while(line!=null) 
		{ 
		System.out.println(line); 
		line=reader.readLine(); 
		} 

		} 
		catch(IOException e1) {} 
//		catch(InterruptedException e2) {} 

		System.out.println("Done"); 
		} 

	public static BufferedReader runCommand(String command, BufferedWriter out, BufferedReader err, boolean wait) {
		BufferedReader in =null;
		try {
			Process p=Runtime.getRuntime().exec(command);
			out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			err = new BufferedReader(new InputStreamReader (p.getErrorStream ()));
			in  = new BufferedReader(new InputStreamReader (p.getInputStream ()));
			if(wait) p.waitFor(); 
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return in;
		
		
	}
}
	
