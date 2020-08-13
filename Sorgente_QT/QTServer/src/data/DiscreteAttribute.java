package data;

import java.util.Iterator;
import java.util.TreeSet;

public class DiscreteAttribute extends Attribute implements Iterable<String> {
	
	private TreeSet<String> values;
	
	public DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name,index);
		this.values=values;
	}
	
	public int getNumberOfDistinctValues() {
		return values.size();
	}

	public Iterator<String> iterator(){
		return values.iterator();
	}
}
