package KeyValueStorage;

import java.util.ArrayList;

import edu.cs.dbms.backend.model.Entry;

public class KeyValueStoring {
	private String DatabaseName;
	private String TableName;
	
	public KeyValueStoring(String db, String t){
		this.DatabaseName = db;
		this.TableName = t;
	}
	
	public ArrayList<Entry> getAllEntries(){
		return null;
		
	}
	
	public Entry getEntry(int id){
		return null;
	}
	
	public boolean insert(Entry e){
		return false;
	}
	
	public boolean delete(Entry e){
		return false;
	}
}
