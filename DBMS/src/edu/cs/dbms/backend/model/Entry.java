package edu.cs.dbms.backend.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.DatabaseEntry;

public class Entry {
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private ArrayList<String> value;

	public ArrayList<String> getValue() {
		return value;
	}
	public void setValue(ArrayList<String> value) {
		this.value = value;
	}
	
	public ArrayList<String> entryToObject(DatabaseEntry entry) {
		 
	    TupleInput input = TupleBinding.entryToInput(entry);
	    
	    ArrayList<String> myList = new ArrayList<String>(Arrays.asList(input.toString().split(",")));
	    return myList;
	}
	public DatabaseEntry objectToEntry() {
		  
		    TupleOutput output = new TupleOutput();
		    DatabaseEntry entry = new DatabaseEntry();
		 
		    // write name, email and department to tuple
		    output.writeString(getValue().toString());
		    TupleBinding.outputToEntry(output, entry);
		 
	   return entry;
	}
}
