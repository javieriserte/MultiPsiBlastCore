package multipsi.filteridentical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.Iterator;

import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;

import multipsi.GroupResultData;

public class FilterIndenticalGroups {
	
	private Map<String,File> paths;
	private File outpath;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[1];
		args[0] = "--configfile C:\\JAvier\\JavaWorkspace\\MultiPsiBlastCore\\configFilter.file";
		cmdGA.Parser parser = new cmdGA.Parser();
		SingleOption configFile = new SingleOption(parser, null, "--configfile", "-cf" , InFileParameter.getParameter());
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.out.println("There was an error while parsing the command line");
		}
		
		if (!configFile.isPresent()) {
			System.out.println("--configfile option is required");
		};
		
		FilterIndenticalGroups fig = new FilterIndenticalGroups();
		
try {   
	
	    
		fig.readConfigFile((File)configFile.getValue());       
	    } catch (IOException e) { e.printStackTrace(); }

		Iterator<Map.Entry<String, File>> it = fig.paths.entrySet().iterator();
		
		
		while(it.hasNext()) {
			Map.Entry<String, File> current = it.next();
			
			File currentPath = current.getValue();
			Set<GroupResultData> group=null;
			for (File f :currentPath.listFiles()) {
				ParseGroupText pgt = new ParseGroupText();
				
try { 			group = pgt.parse(f);                     				} catch (IOException e) {  e.printStackTrace(); }

				group = fig.filterIdenticals(group);
				
				if(!fig.outpath.exists()) fig.outpath.mkdir();
				
				fig.exportFile(new File(fig.outpath+File.separator+current.getKey()+File.separator+f.getName()),group);

			}
			
		}


	}
	
	private void exportFile(File file, Set<GroupResultData> group) {
		PrintWriter pw = null;
		
		if(!file.exists())
			try {
				file.getParentFile().mkdir();
				file.createNewFile();
			} catch (IOException e1) { e1.printStackTrace(); }
		
try {   pw = new PrintWriter(file);          	} catch (FileNotFoundException e) { e.printStackTrace(); }

		Iterator<GroupResultData> it = group.iterator();
		
		while(it.hasNext()) {
			pw.println(it.next().toString());
		}
		pw.flush();
		pw.close();
	}

	public Set<GroupResultData> filterIdenticals(Set<GroupResultData> group) {
		
		Set<GroupResultData> result = new HashSet<GroupResultData>();

		Iterator<GroupResultData> it = group.iterator();
		
		while(it.hasNext()) {
			GroupResultData g = it.next();
			if (!this.contains(result,g)) 
				result.add(g);
		}
		
		return result;
	}
	
	private boolean contains(Set<GroupResultData> groupSet, GroupResultData tested) {
		Iterator<GroupResultData> it = groupSet.iterator();
		boolean found = false;
		while(it.hasNext()&&!found) {
			found = it.next().equals(tested);
		}
		
		return found;
		
	}
	
	
	private void readConfigFile(File cf) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(cf));
		String line;
		Map<String,File> result = new HashMap<String,File>();
		while ((line = bf.readLine())!=null) {
			// Each Line are expected to be :
			// [Field];value;value2 (if apply value2)
			// Fields are: ResultPath
			
			String[] data = line.split(";");
			String field = data[0].replaceAll(" ", "").toUpperCase();
			if (field.equals("[" + "OUTPUTPATH" + "]")) {
				this.outpath = new File(data[1]);
			} else 
			if (field.equals("[" + "INPUTPATH" + "]")) {
				result.put(data[1], new File(data[2]));
			};			
		}
		this.paths =result;
	}

}
