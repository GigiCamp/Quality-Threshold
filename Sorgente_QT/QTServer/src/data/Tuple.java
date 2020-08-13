package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class Tuple implements Serializable {
	
	private Item [] tuple;
	
	public Tuple(int size) {
		tuple=new Item[size];
	}
	
	public int getLength() {
		return tuple.length;
	}
	
	public Item get(int i) {
		return tuple[i];
	}
	
	public void add(Item c, int i) {
		tuple[i]=c;
	}
	
	public String toString() {
		String output="";
		output="(";
		for(int i=0;i<tuple.length;i++) {
			output+=tuple[i].toString();
		}
		output+=")";
		return output;
	}
	
	public double getDistance(Tuple obj) {
		double dist=0;
		for(int i=0;i<this.getLength();i++) 
		{
			dist+=this.get(i).distance(obj.get(i));
		}
		return dist;
	}
	
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		double p=0.0,sumD=0.0; 
		Iterator<Integer> it=clusteredData.iterator();
		while(it.hasNext()){
			Integer a=it.next();
			double d= getDistance(data.getItemSet(a)); 
			sumD+=d; 
		}
		p=sumD/clusteredData.size(); 
		return p;
	}
	
}
