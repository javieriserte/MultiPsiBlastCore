package multipsi.filters;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.Iterator;

import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;

import multipsi.results.GroupResultData;

public class FilterIndenticalGroups extends FilterSetGroup {
	
	/**
	 * @param args
	 */

	@Override
	public Set<GroupResultData> filter(Set<GroupResultData> group) {
		
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



}
