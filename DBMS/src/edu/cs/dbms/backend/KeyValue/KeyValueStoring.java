package edu.cs.dbms.backend.KeyValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import edu.cs.dbms.backend.model.Entry;



public class KeyValueStoring {
	private String DatabaseName;
	private String TableName;
	private EnvironmentConfig envConf;
	private DatabaseConfig dbConf;
	private Database testDB;
	private Environment dbEnv = null;
	public KeyValueStoring(String db, String t){
		envConf = new EnvironmentConfig();
		this.DatabaseName = db;
		this.TableName = t;
		System.out.println(db);
		System.out.println(t);
		envConf.setAllowCreate(true);
		new File("databases").mkdir();
		new File("databases/"+ db).mkdir();
		 // environment will be created if not exists
		
		try {
			dbEnv = new Environment(new File("databases/"+db), envConf);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConf = new DatabaseConfig();
		dbConf.setAllowCreate(true);
		try {
			testDB = dbEnv.openDatabase(null, this.TableName, dbConf);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	   
	public boolean insert(String _key, String _data){
		 DatabaseEntry key = new DatabaseEntry();
		 DatabaseEntry data = new DatabaseEntry();
		 StringBinding.stringToEntry(_key, key);
		 StringBinding.stringToEntry(_data, data);
		 
		 try{if ((testDB.get(null, key, data, null)
		            == OperationStatus.SUCCESS)) {
		  	  System.out.println("mar letezik");
		  	testDB.close();
		    dbEnv.close();
		  	  return false;
		      // assign new values from dataentry to emp
		    } else {
		      System.out.println("beszur");
		      // insert into database
		      testDB.put(null, key, data);
		      testDB.close();
			    dbEnv.close();
		      return true;
		    }
		  } catch (DatabaseException dbe) {
		    System.out.println("Error :" + dbe.getMessage());
		  }
		 return false;
	}
	
	public boolean exists(String _key, String _data){
		 DatabaseEntry key = new DatabaseEntry();
		 DatabaseEntry data = new DatabaseEntry();
		 StringBinding.stringToEntry(_key, key);
		 StringBinding.stringToEntry(_data, data);
		 
		 try{if ((testDB.get(null, key, data, null)
		            == OperationStatus.SUCCESS)) {
		  	  System.out.println("mar letezik");
		  	  
		  	testDB.close();
		    dbEnv.close();
		  	  return false;
		      // assign new values from dataentry to emp
		    } else {
		      System.out.println("letezik");
		      // insert into database
		      testDB.close();
			    dbEnv.close();
		      return true;
		    }
		  } catch (DatabaseException dbe) {
		    System.out.println("Error :" + dbe.getMessage());
		  }
		 return false;
	}
	
	public boolean delete(String _key){
		 DatabaseEntry key = new DatabaseEntry();
		 StringBinding.stringToEntry(_key, key);
		 
		 System.out.println(key+"kitorolni keszul" + key.toString());
		 try{if ((testDB.delete(null, key)
		            == OperationStatus.SUCCESS)) {
		  	  System.out.println("torolve");
		  	  
		  	testDB.close();
		    dbEnv.close();
		  	  return false;
		      // assign new values from dataentry to emp
		    } else {
		      System.out.println("nem letezett");
		      // insert into database
		      testDB.close();
			    dbEnv.close();
		      return true;
		    }
		  } catch (DatabaseException dbe) {
		    System.out.println("Error :" + dbe.getMessage());
		  }
		 return false;
	}

	
	public ArrayList<Entry> getAll(){
		Cursor cursor = null;
		try {
		   

		    // Open the cursor. 
		    cursor = testDB.openCursor(null, null);
		    ArrayList<Entry> list = new ArrayList<>();
		    // Cursors need a pair of DatabaseEntry objects to operate. These hold
		    // the key and data found at any given position in the database.
		    DatabaseEntry foundKey = new DatabaseEntry();
		    DatabaseEntry foundData = new DatabaseEntry();

		    // To iterate, just call getNext() until the last database record has been 
		    // read. All cursor operations return an OperationStatus, so just read 
		    // until we no longer see OperationStatus.SUCCESS
		    while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
		        OperationStatus.SUCCESS) {
		        // getData() on the DatabaseEntry objects returns the byte array
		        // held by that object. We use this to get a String value. If the
		        // DatabaseEntry held a byte array representation of some other data
		        // type (such as a complex object) then this operation would look 
		        // considerably different.
		        String keyString = new String(foundKey.getData());
		        String dataString = new String(foundData.getData());
		        System.out.println("Key | Data : " + keyString + " | " + 
		                       dataString + "");
		       //Entry e = new Entry();
		       //e.setId(Integer.parseInt(keyString));
		       //e.setValue(new ArrayList<String>(Arrays.asList(dataString.toString().split(","))));
		       // list.add(e);
		    }
		} catch (DatabaseException de) {
		    System.err.println("Error accessing database." + de);
		} finally {
		    // Cursors must be closed.
		    try {
				cursor.close();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
   



    // read from database (find key=2)
    
}
