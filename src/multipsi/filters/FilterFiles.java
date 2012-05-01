package multipsi.filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.StringParameter;

import multipsi.ParseGroupText;
import multipsi.IO.ConfigFileTags;
import multipsi.IO.ReadConfigFile;
import multipsi.results.GroupResultData;

public class FilterFiles {
	public static final String OUTPUTPATH = "OUTPUTPATH";
	public static final String INPUTPATH = "INPUTPATH";
	public static final String MORETHAN50 = "MORETHAN50";
	public static final String IDENTICAL = "IDENTICAL";
	public static final String UNIQUE = "UNIQUE";
	public static final String UNIQUEGROUPS = "uniquegroups";
		
	
	
	/**
	 * Main Executable
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[1];
		args[0] = "--filter moreThan50 --configfile C:\\JAvier\\JavaWorkspace\\MultiPsiBlastCore\\configFilter.file";
		cmdGA.Parser parser = new cmdGA.Parser();
		SingleOption configFile = new SingleOption(parser, null, "--configfile", "-cf" , InFileParameter.getParameter());
		SingleOption filterType = new SingleOption(parser, null, "--filter", StringParameter.getParameter());
		
try {   parser.parseEx(args);		             } catch (IncorrectParameterTypeException e) {
			                                              System.out.println("There was an error while parsing the command line"); }
		
    	if (!configFile.isPresent()) {
			System.out.println("--configfile option is required");
		} else 
		if (!filterType.isPresent()) {
			System.out.println("--filter option is required");
		}
    	
    	String filter = ((String) filterType.getValue()).toUpperCase();
    	
    	FilterFiles ff = new FilterFiles();
   		Map<ConfigFileTags, Object> r = null;
   		
try {   r = ff.readConfigFile((File)configFile.getValue());      } catch (IOException e) { 
		                                                                  System.out.println("Therewas an error reading the config file"); }
    	
    	if (filter.equals(MORETHAN50)) {
    		File o = (File) r.get(ConfigFileTags.OutputPath);
    		@SuppressWarnings("unchecked")
			Map<String,File> i = (Map<String,File>) r.get(ConfigFileTags.InputPath);
    		ff.filterMoreThan(o,i,50);
    	} else 
    	if (filter.equals(IDENTICAL)) {
    		File o = (File) r.get(ConfigFileTags.OutputPath);
    		@SuppressWarnings("unchecked")
			Map<String,File> i = (Map<String,File>) r.get(ConfigFileTags.InputPath);
    		ff.filterIdenticals(o,i);
    		
    	} else 
        if (filter.equals(UNIQUE)) {
    		File o = (File) r.get(ConfigFileTags.OutputPath);
    		@SuppressWarnings("unchecked")
			Map<String,File> i = (Map<String,File>) r.get(ConfigFileTags.InputPath);
    		ff.filterUnique(o,i);
        		
    	} else 
		if (filter.equals(UNIQUEGROUPS)) {
    		File o = (File) r.get(ConfigFileTags.OutputPath);
    		@SuppressWarnings("unchecked")
			Map<String,File> i = (Map<String,File>) r.get(ConfigFileTags.InputPath);
    		ff.filterUniqueGroups(o,i);
        		
        }
		
	}
	/**
	 * 
	 * @param o
	 * @param i
	 */
	private void filterUniqueGroups(File outputpath, Map<String, File> inputpath) {
		Iterator<Map.Entry<String, File>> it = inputpath.entrySet().iterator();
		Set<GroupResultData> result = new TreeSet<GroupResultData>();
		FilterUniqueSets fus = new FilterUniqueSets();

		while(it.hasNext()) {
			
			Map.Entry<String, File> current = it.next();
			
			File currentPath = current.getValue();
			
			Set<GroupResultData> group=null;
			
			ParseGroupText pgt = new ParseGroupText();
			
			for (File f :currentPath.listFiles()) {
				
try { 			group = pgt.parse(f);                     				} catch (IOException e) {  e.printStackTrace(); }

				result.addAll(fus.filter(group));
			}
			
		}
		this.exportFile(new File(outputpath+File.separator+"uniqueSets.txt"),result);
	
	}
	/**
	 * 
	 * @param o
	 * @param i
	 */
	private void filterUnique(File outputpath, Map<String, File> inputpath) {
		FilterUniques fig = new FilterUniques();
		Set<GroupResultData> result = filterSingle(inputpath, fig);
		if(!outputpath.exists()) outputpath.mkdir();
		this.exportFile(new File(outputpath+File.separator + "uniques.txt"),result);

	}

	/**
	 * 
	 * @param o
	 * @param i
	 */
	private void filterMoreThan(File outputpath, Map<String, File> inputpath,int value) {
		FilterByElementsFound fig = new FilterByElementsFound(value);
		Set<GroupResultData> result = filterSingle(inputpath, fig);
		if(!outputpath.exists()) outputpath.mkdir();
		this.exportFile(new File(outputpath+File.separator + "moreThan"+value+".txt"),result);
		
	}
	private Set<GroupResultData> filterSingle(Map<String, File> inputpath, FilterSingleGroup fig) {
		Iterator<Map.Entry<String, File>> it = inputpath.entrySet().iterator();
		Set<GroupResultData> result=new HashSet<GroupResultData>();
		while(it.hasNext()) {
			
			Map.Entry<String, File> current = it.next();
			
			File currentPath = current.getValue();
			
			Set<GroupResultData> group=null;
			
			for (File f :currentPath.listFiles()) {
				
				ParseGroupText pgt = new ParseGroupText();
				
try { 			group = pgt.parse(f);                     				} catch (IOException e) {  e.printStackTrace(); }

				Iterator<GroupResultData> it2 = group.iterator();

				while (it2.hasNext()) {
					GroupResultData currentGRD = it2.next();
					
					if (fig.filter(currentGRD))  result.add(currentGRD);
					
				}
				
			}
			
		}
		return result;
	}

	/**
	 * Filter Identicals groups of data.
	 * Given a set of <code>GroupResultData</code> objects, keep those had different elements.
	 * @param outputpath
	 * @param inputpath
	 */
	private void filterIdenticals(File outputpath, Map<String, File> inputpath) {
		FilterIndenticalGroups fig = new FilterIndenticalGroups();
		Iterator<Map.Entry<String, File>> it = inputpath.entrySet().iterator();
		
		while(it.hasNext()) {
			
			Map.Entry<String, File> current = it.next();
			
			File currentPath = current.getValue();
			
			Set<GroupResultData> group=null;
			
			for (File f :currentPath.listFiles()) {
				
				ParseGroupText pgt = new ParseGroupText();
				
try { 			group = pgt.parse(f);                     				} catch (IOException e) {  e.printStackTrace(); }

				group = fig.filter(group);
				
				if(!outputpath.exists()) outputpath.mkdir();
				
				this.exportFile(new File(outputpath+File.separator+current.getKey()+File.separator+f.getName()),group);

			}
			
		}
		
	}

	/**
	 * Prints out the data of a result group to a given file.
	 * @param file the output file. 
	 * @param group the data to be written to disk
	 */
	protected void exportFile(File file, Set<GroupResultData> group) {
		PrintWriter pw = null;
		
		if(!file.exists())

try {   file.getParentFile().mkdir(); 

	    file.createNewFile();        			
		
        pw = new PrintWriter(file);          	} catch (FileNotFoundException e) { e.printStackTrace(); } 
                                                  catch (IOException e) { e.printStackTrace(); 	}

		Iterator<GroupResultData> it = group.iterator();
		
		while(it.hasNext()) {

			pw.println(it.next().toString());
		
		}

		pw.flush();
		
		pw.close();
	}

	/**
	 * reads a config file in a generic way in order to be used by any class that needs to reads this type of configfiles.
	 * @param cf
	 * @return
	 * @throws IOException
	 */
	public Map<ConfigFileTags,Object> readConfigFile(File cf) throws IOException {

		Map<String,File> resultPaths = new HashMap<String,File>();
		Map<ConfigFileTags,Object> result = new HashMap<ConfigFileTags,Object>();

		ReadConfigFile rcf = new ReadConfigFile();
		
		Map<ConfigFileTags, List<List<String>>> read  = rcf.readConfigFile(cf);
		
		result.put(ConfigFileTags.OutputPath,(new File(read.get(ConfigFileTags.OutputPath).get(0).get(0))));
		
		List<List<String>> inpaths = read.get(ConfigFileTags.InputPath);
		
		Iterator<List<String>> it = inpaths.iterator();
		while (it.hasNext()) {
			List<String> values = it.next();
			resultPaths.put(values.get(0), new File(values.get(1)));
		}
		result.put(ConfigFileTags.InputPath, resultPaths);
		
		return result;
	}
}
