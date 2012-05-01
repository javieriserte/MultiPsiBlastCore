package multipsi.filters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import multipsi.results.GroupResultData;

public class FilterUniqueSets extends FilterSetGroup{

	@Override
	public Set<GroupResultData> filter(Set<GroupResultData> group) {
		
		Set<GroupResultData> result = new HashSet<GroupResultData>();
		
		Iterator<GroupResultData> it = group.iterator();
		GroupResultData last =null;
		
		boolean different = false;
		while(it.hasNext() && !different) {
			last =it.next();
			different = last.getElementsFound() != 1;
			
		}
		if (!different) result.add(last);
		return result;

	}

}
