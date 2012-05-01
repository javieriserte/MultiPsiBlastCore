package multipsi.filters;

import multipsi.results.GroupResultData;

public class FilterByElementsFound extends FilterSingleGroup{
	private int value;
	
	
	public FilterByElementsFound(int value) {
		super();
		this.value = value;
	}


	/**
	 * returns true if the given group has equal or more than a value of elements
	 */
	@Override
	public boolean filter(GroupResultData group) {
		return group.getElementsFound() >= this.value;
	}

}
