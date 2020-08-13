package mining;
import java.io.Serializable;
import java.util.Iterator;

import java.util.Set;
import java.util.TreeSet;

import data.Data;


public class ClusterSet implements Iterable<Cluster>, Serializable{
	
	private Set<Cluster> C= new TreeSet<Cluster>();
	
	ClusterSet() {}
	
	public void add(Cluster c) {
		C.add(c);
	}
	
	public String toString() {
		String s="";
		Iterator<Cluster> it=C.iterator();
		while(it.hasNext()) {
			Cluster a=it.next();
			s=s+a.getCentroid().toString();
			s+="\n";
		}
		
		return s;
	}
	
	public String toString(Data data) {
		String str="";
		Iterator<Cluster> it=C.iterator();
		int i=0;
		while(it.hasNext()){
			Cluster a=it.next();
			if (a!=null){ 
				str+=i+":"+a.toString(data)+"\n"; 
			}
			i++;
		}
		return str;
	}
	
	public Iterator<Cluster> iterator(){
		return C.iterator();
	}
	
}
	
	
	