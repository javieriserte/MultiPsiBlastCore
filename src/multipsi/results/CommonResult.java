package multipsi.results;


public class CommonResult {
	private GroupResultData group;
	private int timesfound;
	
	
	
	public CommonResult(GroupResultData group, int timesfound) {
		super();
		this.group = group;
		this.timesfound = timesfound;
	}
	
	
	public GroupResultData getGroup() {
		return group;
	}
	public void setGroup(GroupResultData group) {
		this.group = group;
	}
	public int getTimesfound() {
		return timesfound;
	}
	public void setTimesfound(int timesfound) {
		this.timesfound = timesfound;
	}


	@Override
	public String toString() {
		return "Group found=" + timesfound + " of " + group.getElementsFound() + " " + group.proteinsFound();
	} 
	
	
}
