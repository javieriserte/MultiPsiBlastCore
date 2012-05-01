package multipsi.filters;

import java.util.Set;

import multipsi.results.GroupResultData;

public abstract class FilterSetGroup extends Filter{

	public abstract Set<GroupResultData> filter(Set<GroupResultData> group); 
	


}
