package mining;
import java.io.Serializable;
import java.util.HashSet;

import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;


class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable{
	
	private Tuple centroid;
	private Set<Integer> clusteredData;
	
	/*Cluster(){
		
	}*/

	Cluster(Tuple centroid){
		this.centroid=centroid;
		clusteredData=new HashSet<Integer>();
	}
		
	Tuple getCentroid(){
		return centroid;
	}
	
	//return true if the tuple is changing cluster
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	
	//verifica se una transazione è clusterizzata nell'array corrente
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	

	//remove the tuplethat has changed the cluster
	void removeTuple(int id){
		clusteredData.remove(id);
		
	}
	
	int  getSize(){
		return clusteredData.size();
	}
	
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i);
		str+=")";
		return str;
		
	}
	
	public String toString(Data data){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		Set<Integer> array=clusteredData;
		Iterator<Integer> it=array.iterator();
		while(it.hasNext()){
			Integer a=it.next();
			str+="[";
			for(int j=0;j<data.getNumberOfExplanatoryAttributes();j++)
				str+=data.getValue(j)+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet(a))+"\n";
			
		}
		str+="\nAvgDistance="+getCentroid().avgDistance(data, array);
		return str;
	}

	@Override
	public int compareTo(Cluster P) {
		if(this.getSize()>P.getSize()){
			return 1;
		}else {
			return -1;
		}
	}
	
	public Iterator<Integer> iterator(){
		return clusteredData.iterator();
	}
}
