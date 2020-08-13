package data;

public class ContinuousItem extends Item {

	ContinuousItem(Attribute attribute, Double value) {
		super(attribute, value);
	}
	
	public double distance(Object a) {
		ContinuousAttribute b=(ContinuousAttribute) this.getAttribute();
		ContinuousItem c=(ContinuousItem) a;
		double i=b.getScaledValue((double)this.getValue());
		double j=b.getScaledValue((double)c.getValue());
		return Math.abs(i-j);
	}	
}

