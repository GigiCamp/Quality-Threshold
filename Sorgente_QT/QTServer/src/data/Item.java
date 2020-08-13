package data;

import java.io.Serializable;

public abstract class Item implements Serializable {

	private Attribute attribute;
	private Object value;
	
	public Item(Attribute attribute, Object value) {
		this.attribute=attribute;
		this.value=value;
	}
	
	public Attribute getAttribute() {
		return this.attribute;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.value.toString();
	}
	
	public abstract double distance(Object a);
		
}
