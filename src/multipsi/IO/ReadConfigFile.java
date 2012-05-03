package multipsi.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadConfigFile {
	private static final String OUTPUTPATH = "OUTPUTPATH";
	private static final String INPUTPATH = "INPUTPATH";
	
	private Map<String,ConfigFileTags> tagMap = null;

	
	
	public ReadConfigFile() {
		super();
		this.tagMap = new HashMap<String, ConfigFileTags>();
		this.tagMap.put(OUTPUTPATH, ConfigFileTags.OutputPath);
		this.tagMap.put(INPUTPATH, ConfigFileTags.InputPath);
	}

	/**
	 * reads a config file in a generic way in order to be used by any class that needs to reads this type of configfiles.
	 * @param cf
	 * @return
	 * @throws IOException
	 */
	public Map<ConfigFileTags,List<List<String>>> readConfigFile(File cf) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(cf));
		String line;
		Map<ConfigFileTags,List<List<String>>> result = new HashMap<ConfigFileTags,List<List<String>>>();
		while ((line = bf.readLine())!=null) {
			// Each Line are expected to be :
			// [Field];value;value2 (if apply value2)
			// Fields are: ResultPath
			
			String[] data = line.split(";");
			String field = data[0].replaceAll(" ", "").toUpperCase();
			
			field = field.replaceAll("\\[", "").replaceAll("\\]", "");
			ConfigFileTags tag = null;
			if (this.tagMap.containsKey(field)) tag = this.tagMap.get(field);
			else throw new IOException("Field Tag not Recognized: "+field);
			
			if(!result.containsKey(tag)) result.put(tag, new ArrayList<List<String>>());
			
			List<String> values = new ArrayList<String>();
			for (int i=1;i<data.length;i++) {
				values.add(data[i]);
			}
			
			result.get(tag).add(values);
		}
		return result;
	}
	
	
	
	
	
}
