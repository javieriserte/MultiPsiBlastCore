package multipsi.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import multipsi.results.GroupResultData;

public class ParseGroupText {

	public Set<GroupResultData> parse (File infile) throws IOException {

		Set<GroupResultData> groupSet = new HashSet<GroupResultData>();
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line;
		while (( line = br.readLine())!=null) {
			GroupResultData g = this.parseline(line);
			if(g!=null) groupSet.add(g);
		}
		return groupSet;
		
	}
	
	public GroupResultData parseline(String line ){
		
//		String s = "	With Protein: APO|006 evalue: 0.1 evalue Threshold: 1.0E-8 Found: 26 elements: [MCB|035, LDN|128, TNN|136, HAN|034, HZN|131, SEN|028, XCG|113, SLN|025, EON|121, SF9|026, SF2|027, APO|006, HAG|117, CBN|130, HA4|127, CCN|142, EUN|131, HA1|129, ASN|029, MC4|039, LXN|129, AIN|033, OLN|130, AHN|121, MCN|040, AON|117]".trim();
		line = line.trim();
		Pattern p = Pattern.compile("With Protein: (.+) evalue: (.+) evalue Threshold: (.+) Found: (.+) elements: \\[(.+)\\]");
		Matcher m = p.matcher(line);
		
		if (m.matches()) {
			String initialProtein = m.group(1);
			String[] codes = initialProtein.split("\\|");
			String initialGenome = codes[0];
			Double eValue = Double.valueOf(m.group(2));
			Double eValueTh = Double.valueOf(m.group(3));
			Integer nElementsFound = Integer.valueOf(m.group(4));
			String[] pfound = m.group(5).split(", ");
			Set<String> proteinSet = new HashSet<String>();
			for (String pr : pfound) proteinSet.add(pr);
			return new GroupResultData(initialGenome, initialProtein, eValue, eValueTh, nElementsFound,proteinSet);
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		ParseGroupText pgt = new ParseGroupText();
		System.out.println(pgt.parseline(""));
		
	}
	
}
