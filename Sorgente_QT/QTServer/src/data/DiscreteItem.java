package data;

public class DiscreteItem extends Item {

	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
	
	public double distance(Object a) {
		DiscreteItem b=(DiscreteItem) a;
		if(getValue().equals(b.toString())) 
			return 0;
		else
			return 1;
	}
}
