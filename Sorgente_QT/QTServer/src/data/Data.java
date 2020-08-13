package data;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

public class Data {
	/**
	 * 
	 */
	private List<Example> data = new ArrayList<Example>();
	/*private Object data [][]={{"Sunny","Hot","High","Weak","No"},
							  {"Sunny","Hot","High","Strong","No"},
							  {"Overcast","Hot","High","Weak","Yes"},
							  {"Rain","Mild","High","Weak","Yes"},
							  {"Rain","Cool","Normal","Weak","Yes"},
							  {"Rain","Cool","Normal","Strong","No"},
							  {"Overcast","Cool","Normal","Strong","Yes"},
							  {"Sunny","Mild","High","Weak","No"},
							  {"Sunny","Cool","Normal","Weak","Yes"},
							  {"Rain","Mild","Normal","Weak","Yes"},
							  {"Sunny","Mild","Normal","Strong","Yes"},
							  {"Overcast","Mild","High","Strong","Yes"},
							  {"Overcast","Hot","Normal","Weak","Yes"},
							  {"Rain","Mild","High","Strong","No"}};*/
	private int numberOfExamples;
	private List<Attribute> explanatorySet=new LinkedList<Attribute>();

	public Data(String table) throws EmptyDatasetException, ClassNotFoundException, DatabaseConnectionException, SQLException, EmptySetException, InstantiationException, IllegalAccessException, NoValueException  {

		DbAccess DataBase=new DbAccess();
		TableData t=new TableData(DataBase);
		data=t.getDistinctTransazioni(table);
		
		if(data.size()==0 || data==null) throw new EmptyDatasetException(numberOfExamples);

		TableSchema s=new TableSchema(DataBase, table);
		for(int i=0;i<s.getNumberOfAttributes();i++) {
			Column c=s.getColumn(i);
			if(c.isNumber()==false) {
				Set<Object> l= t.getDistinctColumnValues(table, c);
				TreeSet<String> ts=new TreeSet<String>();
				Iterator<Object> it=l.iterator();
				while(!it.hasNext()) 
					ts.add((String) it.next());
				explanatorySet.add(new DiscreteAttribute(c.getColumnName(), i, ts));
				}
				else {
					Double min=((Number) t.getAggregateColumnValue(table, c, QUERY_TYPE.MIN)).doubleValue();
					Double max=((Number) t.getAggregateColumnValue(table, c, QUERY_TYPE.MAX)).doubleValue();
					explanatorySet.add(new ContinuousAttribute(c.getColumnName(), i, min, max));
				}
			}
			numberOfExamples=data.size();
		}

	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	public List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}

	public Object getValue(int index) {
		return data.get(index);
	}

	public String toString() {
		String output = new String();
		output=output+(getAttributeSchema());
		output+="\n";
		for(int j=0;j<getNumberOfExamples();j++) {
			output=output+(j+1)+":";
			for(int k=0;k<getNumberOfExplanatoryAttributes();k++) {
				output=output+" "+getValue(k)+" |";
			}
			output+="\n";
		}
		return output;
	}

	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(explanatorySet.size());
		for (int i = 0; i < explanatorySet.size(); i++) {
			if(explanatorySet.get(i) instanceof DiscreteAttribute) {
				tuple.add(new DiscreteItem((DiscreteAttribute) explanatorySet.get(i), (String) data.get(index).get(i)), i);
			}
			if(explanatorySet.get(i) instanceof ContinuousAttribute) {
				tuple.add(new ContinuousItem((ContinuousAttribute) explanatorySet.get(i), (Double) data.get(index).get(i)), i);
			}
		}
			return tuple;
	}


	public static void main(String args[]) throws ClassNotFoundException, DatabaseConnectionException, SQLException, EmptySetException, InstantiationException, IllegalAccessException, NoValueException {
		Data trainingSet;
		try {
			trainingSet = new Data("MapDB.playtennis");
			System.out.println(trainingSet);
		} catch (EmptyDatasetException e) {}
	}

}
