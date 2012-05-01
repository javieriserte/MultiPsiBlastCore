package multipsi.commonSets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import multipsi.ParseGroupText;
import multipsi.IO.ConfigFileTags;
import multipsi.filters.FilterFiles;
import multipsi.results.CommonResult;
import multipsi.results.GroupResultData;

public class KeepCommonSets {

	private Set<GroupResultData> wanted;
	private Map<String,Set<GroupResultData>> lookFor;
	
	public static void main(String[] args)  {

		
		KeepCommonSets kcs = new KeepCommonSets();
		FilterFiles ff = new FilterFiles();
		Map<ConfigFileTags, Object> cf = null;
try {   cf = ff.readConfigFile(null);         	} catch (IOException e) { e.printStackTrace(); }
		

		File outpath = (File) cf.get(ConfigFileTags.OutputPath);
		@SuppressWarnings("unchecked")
		Map<String,File> paths = (Map<String,File>) cf.get(ConfigFileTags.InputPath);
		
		kcs.createDataStructures(paths);
		
		Set<CommonResult> result = KeepCommonSets.search(kcs.wanted, kcs.lookFor, outpath);
		
		
		exportFile(new File(outpath+File.separator+"commonsSets.txt"),result);
		
	}

	/**
	 * Write the results to a file.
	 * The results are given as a set of KeepCommonResult objects.
	 * @param file
	 * @param group
	 */
	protected static void exportFile(File file, Set<CommonResult> group) {
		PrintWriter pw = null;
		
		if(!file.exists())

try {   file.getParentFile().mkdir(); 

	    file.createNewFile();        			
		
        pw = new PrintWriter(file);          	} catch (FileNotFoundException e) { e.printStackTrace(); } 
                                                  catch (IOException e) { e.printStackTrace(); 	}

		Iterator<CommonResult> it = group.iterator();
		
		while(it.hasNext()) {

			pw.println(it.next().toString());
		
		}

		pw.flush();
		
		pw.close();
	}

	/**
	 * Searches for each group in wanted to be present in looker for dictionary. 
	 * @param outpath
	 * @return a set of KeepCommonResult, that contains all groups in wanted with the number of matches they were found.
	 */
	private static Set<CommonResult> search(Set<GroupResultData> wanted, Map<String,Set<GroupResultData>> lookFor, File outpath) {
		Iterator<GroupResultData> it = wanted.iterator();
		Set<CommonResult> result = new HashSet<CommonResult>();
		
		while(it.hasNext()) {
			result.add(scan(lookFor,it.next()));
		}
		
		return result;
		
	}


	/**
	 * Searches if in the lookFor dictionary appears a given group of results. 
	 * The number of matches for this group are returned.
	 * @param lookFor
	 * @param group
	 * @return
	 */
	private static CommonResult scan(Map<String,Set<GroupResultData>> lookFor, GroupResultData group) {
		group.newIteration();
		
		int timesfound=0;
		while(group.hasNext()) {
			String proteinId = group.next();
			
			Set<GroupResultData> others = lookFor.get(proteinId);
			
			for(GroupResultData g : others) {
				
				
				if (group.sameProteinsFound(g)) {

					timesfound++;

					others.remove(g);
					
				}
			}
			
		}
		return new CommonResult(group, timesfound);
		
	}

	/**
	 * Creates suitable data structures for the program.
	 * @param kcs
	 * @param paths
	 */
	private void createDataStructures(Map<String, File> paths) {
		ParseGroupText pgt = new ParseGroupText();
		Iterator<Map.Entry<String,File>> it = paths.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,File> e = it.next();
			
			String proteinID = e.getKey();
			File in = e.getValue();
			Set<GroupResultData> r = null;
			
try {		r = pgt.parse(in);  		} catch (IOException e1) { e1.printStackTrace(); }

			Iterator<GroupResultData> it2 = r.iterator();
			
			while (it2.hasNext()) {
				GroupResultData next = it2.next();
				this.addDataGroupTo(this.lookFor,proteinID, next);
				this.wanted.add(next);
			}
		}
	}
	
	/**
	 * Auxiliary method to construct the data structures needed by the program.
	 * @param colection
	 * @param proteinID
	 * @param group
	 */
	private void addDataGroupTo(Map<String,Set<GroupResultData>> colection, String proteinID, GroupResultData group ) {
		if(!colection.containsKey(proteinID)) {
			colection.put(proteinID, new HashSet<GroupResultData>());
		}
		colection.get(proteinID).add(group);
	}
	
	
	
	
	
}
