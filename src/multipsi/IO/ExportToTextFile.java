package multipsi.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.lang.Iterable;

public class ExportToTextFile {

	public static void ExportObjects(File outfile, Iterable<? extends Object> objects ) {
		PrintWriter pw = null;
		
		if(!outfile.exists())

try {   outfile.getParentFile().mkdir(); 

		outfile.createNewFile();        			
		
        pw = new PrintWriter(outfile);          	} catch (FileNotFoundException e) { e.printStackTrace(); } 
                                                      catch (IOException e) { e.printStackTrace(); 	}

		Iterator<?> it = objects.iterator();
		
		while(it.hasNext()) {

			pw.println(it.next().toString());
		
		}

		pw.flush();
		
		pw.close();
	}
	
}
