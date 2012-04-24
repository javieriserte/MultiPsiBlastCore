package multipsi;

import java.util.Iterator;
import java.util.Set;

public class GroupResultData {
	private String initialGenome;
	private String initialProtein;
	private double eValue;
	private double thEValue;
	private int elementsFound;
	private Set<String> proteinsfound;
	
	private Iterator<String> iterator = null;

	//////////////////////////
	// Constructor
	/**
	 * Constructor method.
	 * 
	 * @param initialGenome
	 * @param initialProtein
	 * @param eValue
	 * @param thEValue
	 * @param proteinfound
	 */
	public GroupResultData(String initialGenome, String initialProtein, double eValue, double thEValue, int elementsfound, Set<String> proteinfound) {
		super();
		this.initialGenome = initialGenome;
		this.initialProtein = initialProtein;
		this.eValue = eValue;
		this.thEValue = thEValue;
		this.proteinsfound = proteinfound;
		this.elementsFound = elementsfound;
	}
	
	//////////////////////
	// Public Interface
	/**
	 * Verify that two Groups had the same elements and no others.
	 * @param other
	 * @return
	 */
	public boolean sameProteinsFound(GroupResultData other) {
		return this.getProteinfound().equals(other.getProteinfound());
	}
	/**
	 * 
	 */
	public void newIteration() {
		this.setIterator(this.getProteinfound().iterator());
	}
	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		if (this.getIterator()!=null) return this.getIterator().hasNext();
		return false;
	}
	/**
	 * 
	 * @return
	 */
	public String next() {
		if (this.getIterator()!=null) return this.getIterator().next();
		return null;
	}
	@Override
	public String toString() {
		return "With Protein: " + this.getInitialProtein() +
		       " evalue: "+ this.geteValue()+
		       " evalue Threshold: " + this.getThEValue()+
		       " Found: " + this.getElementsFound()+
		       " elements: "+this.getProteinfound();
	}
	
	///////////////////////
	// Getters and Setters
	
	public String getInitialGenome() {
		return initialGenome;
	}

	protected void setInitialGenome(String initialGenome) {
		this.initialGenome = initialGenome;
	}

	public String getInitialProtein() {
		return initialProtein;
	}

	protected void setInitialProtein(String initialProtein) {
		this.initialProtein = initialProtein;
	}

	public double geteValue() {
		return eValue;
	}

	protected void seteValue(double eValue) {
		this.eValue = eValue;
	}

	public double getThEValue() {
		return thEValue;
	}

	protected void setThEValue(double thEValue) {
		this.thEValue = thEValue;
	}

	protected Set<String> getProteinfound() {
		return proteinsfound;
	}

	protected void setProteinfound(Set<String> proteinfound) {
		this.proteinsfound = proteinfound;
	}

	protected Iterator<String> getIterator() {
		return iterator;
	}

	protected void setIterator(Iterator<String> iterator) {
		this.iterator = iterator;
	}

	public int getElementsFound() {
		return elementsFound;
	}

	protected void setElementsFound(int elementsFound) {
		this.elementsFound = elementsFound;
	}

	protected Set<String> getProteinsfound() {
		return proteinsfound;
	}

	protected void setProteinsfound(Set<String> proteinsfound) {
		this.proteinsfound = proteinsfound;
	}
}
