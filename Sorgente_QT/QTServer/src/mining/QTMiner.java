package mining;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import data.Data; 
import data.Tuple;

public class QTMiner {

	private ClusterSet C;
	private double radius;

	public QTMiner(double radius) {
		C=new ClusterSet();
		this.radius=radius;
	}
	
	public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream in=new FileInputStream(fileName);
		ObjectInputStream s=new ObjectInputStream(in);
		C=(ClusterSet)s.readObject();
		s.close();
	}
	
	public void salva(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream out=new FileOutputStream(fileName);
		ObjectOutputStream s=new ObjectOutputStream(out);
		s.writeObject(C);
		s.close();
	}

	public ClusterSet getC() {
		return C;
	}
	
	public String toString() {
		Iterator<Cluster> it=C.iterator();
		String str="";
		int i=1;
		while(it.hasNext()) {
			Cluster a=it.next();
			str+=i+":"+a.toString()+"\n";
			i++;
		}
		return str;
	}

	public int compute(Data data) throws ClusteringRadiusException  {
		int numClusters=0;
		boolean isClustered[]=new boolean[data.getNumberOfExamples()];
		for(int i=0;i<isClustered.length;i++) {
			isClustered[i]=false;
		}

		int countClustered=0;
		while(countClustered!=data.getNumberOfExamples())
		{
			Cluster c=buildCandidateCluster(data, isClustered); 
			C.add(c);
			numClusters++;

			Iterator<Integer> clusteredTupleId=c.iterator(); 
			while(clusteredTupleId.hasNext()){ 
				isClustered[clusteredTupleId.next()]=true; 
			}
			countClustered+=c.getSize(); 
		}
		if(numClusters==1) throw new ClusteringRadiusException(data.getNumberOfExamples());
		else
			return numClusters;
	}

	public Cluster buildCandidateCluster(Data data, boolean isClustered[]) {
		Cluster candidate=null;
		int size;
		int max_size=0;

		for(int i=0;i<data.getNumberOfExamples();i++) {
			size=0;
			if(isClustered[i]==false) {
				Tuple a=data.getItemSet(i);
				Cluster c=new Cluster(a);
				for(int j=0;j<data.getNumberOfExamples();j++) {
					if(isClustered[j]==false) {
						Tuple b=data.getItemSet(j);
						if(a.getDistance(b)<=radius) {
							c.addData(j);
							size++;
						}
					}
				}
				if(size>max_size) {
					max_size=size;
					candidate=c;
				}
			}

		}
		return candidate;
	}
}
