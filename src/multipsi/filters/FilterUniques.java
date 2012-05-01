package multipsi.filters;

import multipsi.results.GroupResultData;

public class FilterUniques extends FilterSingleGroup{

	@Override
	public boolean filter(GroupResultData group) {
		return group.getElementsFound()==1;		
	}

}
