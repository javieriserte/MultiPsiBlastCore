package multipsi.filters;

import multipsi.results.GroupResultData;

public abstract class FilterSingleGroup extends Filter{
	public abstract boolean filter(GroupResultData group);
}
