package multipsi.results;


public class CommonResult implements Comparable<CommonResult>{
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


	@Override
	public int compareTo(CommonResult o) {
		
		int first = o.timesfound - this.timesfound;
		if (first!=0) return first;
		else {
			String tp = this.toString();
			String op = o.toString();
			return tp.compareTo(op);
		}
		
			
	} 
	
	
}
