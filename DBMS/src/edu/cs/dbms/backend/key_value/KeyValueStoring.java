package edu.cs.dbms.backend.key_value;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import edu.cs.dbms.backend.model.Attribute;
import edu.cs.dbms.backend.service.AttributeService;
import edu.cs.dbms.backend.service.DatabaseException;
import edu.cs.dbms.backend.util.Config;

public class KeyValueStoring {

	private EnvironmentConfig envConf;
	private DatabaseConfig dbConf;
	private Database testDB;
	private Environment dbEnv = null;
	private String kvPath;
	
	public KeyValueStoring(){
		this.envConf = new EnvironmentConfig();
		this.envConf.setAllowCreate(true);
		this.dbConf = new DatabaseConfig();
		this.dbConf.setAllowCreate(true);
	}

	//beallitom a path-t h melyik file-val akarok dolgozni
	public void setPath(String path, String kvFile){
	
		this.kvPath = path + "/" + kvFile;
		File file = new File(kvPath);
		file.mkdirs();
		try {
			dbEnv = new Environment(file, envConf);
			testDB = dbEnv.openDatabase(null, kvFile, dbConf);
		} catch (com.sleepycat.je.DatabaseException e) {
			System.out.println("Can't open or creat key value file! " + e.toString());
			throw new DatabaseException("Server is not available!");
		}
	}
	//beszuras adott key-re
	public void insert(String _key, String _data) throws DatabaseException{
		
		try{
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();
			StringBinding.stringToEntry(_key, key);
			StringBinding.stringToEntry(_data, data);
			
			if (!(testDB.get(null, key, data, null)
					== OperationStatus.SUCCESS)) {
				testDB.put(null, key, data);
			} else {
				throw new DatabaseException("This key is alredy exist!");
			}
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Error key value insert: " + dbe.getMessage());
			throw new DatabaseException("Insert failed, try again later!");
		}finally{
			try {
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
	}

	public boolean exists(String _key, String _data){
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		StringBinding.stringToEntry(_key, key);
		StringBinding.stringToEntry(_data, data);

		try{
			if ((testDB.get(null, key, data, null)
					== OperationStatus.SUCCESS)) {
				return true;
			} 
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Error :" + dbe.getMessage());
		}finally{
			try {
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
		return false;
	}

	public void delete(String _key)throws DatabaseException{
		DatabaseEntry key = new DatabaseEntry();
		StringBinding.stringToEntry(_key, key);

		try{
			if (!(testDB.delete(null, key) == OperationStatus.SUCCESS)) {
				throw new DatabaseException("Delet failed, this key does not exist!");
			}
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Error :" + dbe.getMessage());
			throw new DatabaseException("Delet failed, try again later!");
		}finally{
			try {
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
	}

	public Map<String, List<String>> getAll()throws DatabaseException{
		Cursor cursor = null;
		Map<String, List<String>> keyValue = null;

		try {
			// Open the cursor. 
			cursor = testDB.openCursor(null, null);
			// ArrayList<Entry> list = new ArrayList<>();
			// Cursors need a pair of DatabaseEntry objects to operate. These hold
			// the key and data found at any given position in the database.
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			keyValue = new HashMap<String, List<String>>();
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
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				keyValue.put(keyString, dataList);
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Error accessing database." + de);
			throw new DatabaseException("Error accessing database, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}

		return keyValue;
	}

	//felulirok egy adott kulcson levo erteket
	public void updateValue(String key, String data) throws DatabaseException{

		DatabaseEntry dataKey = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();
		StringBinding.stringToEntry(key, dataKey);
		StringBinding.stringToEntry(data, dataEntry);

		try{
			testDB.put(null, dataKey, dataEntry);
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Error key value update: " + dbe.getMessage());
			throw new DatabaseException("Update failed, try again later!");
		}finally{
			try {
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
	}
	
	//path segitsegevel bealitom h 
	//melyik tablarol van szo es visszteriti referencia tablaba levo elsodleges kulcsokat
	public List<String> getForeignKeysByTable()throws DatabaseException{
		
		Cursor cursor = null;
		List<String> keys = new ArrayList<String>();
		try {
	
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
	
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
	
				String key = new String(foundKey.getData());
				String separator = key.charAt(key.length()-1) + "";
				String keyString = key.replace(separator, "");
				keys.add(keyString);
			}
			return keys;
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Error accessing database." + de);
			throw new DatabaseException("Error accessing database, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}
	}
	
	//egy adott primary key-hez lekerem a kulsokulcsot
	public String getForeignKeyByPrimary(String primaryKey)throws DatabaseException{
		Cursor cursor = null;
		try {
			 
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
			
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));

				for(int i = 0; i < dataList.size(); i++){
					if(dataList.get(i).equals(primaryKey)){
						return keyString;
					}
				}
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Error accessing database." + de);
			throw new DatabaseException("Error accessing database, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public void insertPrimaryInForeign(String _key, String _data) throws DatabaseException{
		
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		StringBinding.stringToEntry(_key, key);
		Cursor cursor = null;

		try{
			
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				String k = new String(foundKey.getData());
				String d = new String(foundData.getData());
				String separator = d.charAt(d.length()-1) + "";
				String keyString = k.replace(separator, "");
				String dataString = d.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				if(keyString.equals(_key)){
					if(!dataList.contains(_data)){
						dataList.add(_data);
						StringBinding.stringToEntry(dataList.toString(), data);
						StringBinding.stringToEntry(keyString, key);
						testDB.put(null, key, data);
					}
					return;
					
				}
			}
			
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Error key value insert: " + dbe.getMessage());
			throw new DatabaseException("Insert primary key in foreign keys failed, try again later!");
		}finally{
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
	}
	
	public void deletePrimaryFromForeign(String primaryKey) throws DatabaseException{
		Cursor cursor = null;
		DatabaseEntry entryKey = new DatabaseEntry();
		DatabaseEntry entryData = new DatabaseEntry();
		try {
			 
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
			
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));

				for(int i = 0; i < dataList.size(); i++){
					if(dataList.get(i).equals(primaryKey)){
						dataList.remove(i);
						StringBinding.stringToEntry(dataList.toString(), entryData);
						StringBinding.stringToEntry(keyString, entryKey);
						testDB.put(null, entryKey, entryData);
						return;
					}
				}
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Delete failed, primary form foreign keys." + de);
			throw new DatabaseException("Delete primary from foreign keys failed, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void deletePrimaryFromIndexFile(String primaryKey) throws DatabaseException{
		Cursor cursor = null;
		DatabaseEntry entryKey = new DatabaseEntry();
		DatabaseEntry indexKey = new DatabaseEntry();
		DatabaseEntry entryData = new DatabaseEntry();
		try {
			 
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
			
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				if(dataList.size() == 1){
					StringBinding.stringToEntry(keyString, indexKey);
					if(!(testDB.delete(null,indexKey) == OperationStatus.SUCCESS)){
						throw new DatabaseException("Delete failed from index file!");
					}
					return;
				}
				for(int i = 0; i < dataList.size(); i++){
					if(dataList.get(i).equals(primaryKey)){
						dataList.remove(i);
						StringBinding.stringToEntry(dataList.toString(), entryData);
						StringBinding.stringToEntry(keyString, entryKey);
						testDB.put(null, entryKey, entryData);
						return;
					}
				}
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Delete failed, from index files." + de);
			throw new DatabaseException("Delete failed, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public boolean isForeignKey(String key)throws DatabaseException{
		Cursor cursor = null;
		
		try {
	
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
	
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
	
				String actKey = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = actKey.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				if(key.equals(keyString)&& !dataString.isEmpty()){
					return true;
				}
			}
			return false;
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Error accessing database." + de);
			throw new DatabaseException("Error accessing database, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}
	}
	
	//egy adott path-en torli rekurzivan az egesz mappat
	public static void deleteFile(File file){
		if(file.isDirectory()){

    		//directory is empty, then delete it
    		if(file.list().length==0){

    		   file.delete();
    		   System.out.println("Directory is deleted : "
                                                 + file.getAbsolutePath());

    		}else{

    		   //list all the directory contents
        	   String files[] = file.list();

        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);

        	      //recursive delete
        	     deleteFile(fileDelete);
        	   }

        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     System.out.println("Directory is deleted : "
                                                  + file.getAbsolutePath());
        	   }
    		}

    	}else{
    		//if file, then delete it
    		file.delete();
    		System.out.println("File is deleted : " + file.getAbsolutePath());
    	}
	}
	
	public static boolean fileExists(File file){
		return file.exists();
	}
	
	//a primary key szerinti index filebol torli egy adott attributum osszes erteket
	public void delteAttribute(int clumnIndex)throws DatabaseException{
		Cursor cursor = null;
		Map<String, List<String>> keyValue = null;

		try {
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			keyValue = new HashMap<String, List<String>>();
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				if(dataList.size() >= clumnIndex){
					dataList.remove(clumnIndex - 1);
					keyValue.put(keyString, dataList);
				}
			}
			
			for(Map.Entry<String, List<String>> entry : keyValue.entrySet()) {
				DatabaseEntry dataKey = new DatabaseEntry();
				DatabaseEntry dataEntry = new DatabaseEntry();
				String key = entry.getKey();
				String data = entry.getValue().toString();
				StringBinding.stringToEntry(key, dataKey);
				StringBinding.stringToEntry(data, dataEntry);
				testDB.put(null, dataKey, dataEntry);
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Delete attribute failed." + de);
			throw new DatabaseException("Delete attribute failed, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void insertIndexFile(String indexFileKey, String primaryKey){
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		StringBinding.stringToEntry(indexFileKey, key);
		StringBinding.stringToEntry(primaryKey, data);
		Cursor cursor = null;

		try{
			
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
				if(testDB.get(null, key, data, null) == OperationStatus.SUCCESS){
				while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
						OperationStatus.SUCCESS) {
					String k = new String(foundKey.getData());
					String d = new String(foundData.getData());
					String separator = d.charAt(d.length()-1) + "";
					String keyString = k.replace(separator, "");
					String dataString = d.replace(separator, "").replace("[", "").replace("]", "");
					List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
					if(keyString.equals(indexFileKey)){
						if(!dataList.contains(primaryKey)){
							dataList.add(primaryKey);
							StringBinding.stringToEntry(dataList.toString(), data);
							StringBinding.stringToEntry(keyString, key);
							testDB.put(null, key, data);
						}
						return;	
					}
				}
			}
			StringBinding.stringToEntry(primaryKey, data);
			StringBinding.stringToEntry(indexFileKey, key);
			testDB.put(null, key, data);
			
		} catch (com.sleepycat.je.DatabaseException dbe) {
			System.out.println("Insert failed to index file: " + dbe.getMessage());
			throw new DatabaseException("Insert failed to index file, try again later!");
		}finally{
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Can't close the key value file! " + e.getMessage());
			}
		}
	}
	
	public void updateIndexFile(String indexFileKey, String oldIndexFileKey, 
			String primaryKey, String oldaPrimaryKey) throws DatabaseException{
		Cursor cursor = null;
		try {
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			DatabaseEntry indexKey = new DatabaseEntry();
			DatabaseEntry indexData = new DatabaseEntry();
			boolean done = false;
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				
				//ha uj index key mar letezik
				//lehet ha valtozott a primary key akkor kicsereli
				if(indexFileKey.equals(keyString)){
					dataList.remove(oldaPrimaryKey);
					if(!dataList.contains(primaryKey)){
						dataList.add(primaryKey);
					}
					StringBinding.stringToEntry(indexFileKey, indexKey);
					StringBinding.stringToEntry(dataList.toString(), indexData);
					testDB.put(null, indexKey, indexData);
					done = true;
				//ha modosult az index key
				//torli a primaryt a key mellol, ha nem marad erteke a key-en akkor torli az egesz key-t is
				}else if(keyString.equals(oldIndexFileKey)){
					dataList.remove(oldaPrimaryKey);
					dataList.remove(primaryKey);
					if(dataList.size() == 0){
						StringBinding.stringToEntry(oldIndexFileKey, indexKey);
						StringBinding.stringToEntry(primaryKey, indexData);
						testDB.delete(null, indexKey);
					}else{
						StringBinding.stringToEntry(oldIndexFileKey, indexKey);
						StringBinding.stringToEntry(dataList.toString(), indexData);
						testDB.put(null, indexKey, indexData);
					}	
				}			
			}
			//ha teljesen uj index key
			if(!done){
				List<String> data = new ArrayList<String>();
				data.add(primaryKey);
				StringBinding.stringToEntry(indexFileKey, indexKey);
				StringBinding.stringToEntry(data.toString(), indexData);
				testDB.put(null, indexKey, indexData);
			}
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Update failed in index file." + de);
			throw new DatabaseException("Update failed in index file, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}		
	}

	public Map<String, List<String>> select(String databaseName, String tableName, 
			List<String> attrList, List<List<String>> condition)throws DatabaseException{
		
		Map<String, List<String>> keyValue = new HashMap<String, List<String>>();
		AttributeService attrSv = new AttributeService();
		Attribute attr = new Attribute.AttributeBuilder()
									  .setDatabaseName(databaseName)
									  .setTableName(tableName)
									  .setAttrName("")
									  .creatAttr();
		
		List<String> keys = new ArrayList<String>();
		
		//megyek vegig a felteteleken es felhasznalva az index file-kat ellenorzom h igaz
		for(int i = 0; i < condition.size(); i++){
			List<String> c = condition.get(i);
			attr.setAttrName(c.get(0));
			
			/*if(attrSv.isPrimaryKey(attr)){
				System.out.println("Primary: " + c.get(0));
				List<String> k = searchByKey(c.get(2), c.get(1));
				
				if(k.isEmpty()){
					return keyValue;
				}
				
				if(!keys.isEmpty()){
					for(String s : k){
						if(!keys.contains(s)){
							return keyValue;
						}
					}
				}else{
					keys.addAll(k);
				}
				condition.remove(c);
			}else*/ if(attrSv.isUnique(attr)){
				this.setPath(Config.KEY_VALUE_PATH_UQ + databaseName + "/" + tableName, attr.getAttrName());
				System.out.println("Unique: " + c.get(0));
				List<String> k = searchByKey(c.get(2), c.get(1));
				
				if(k.isEmpty()){
					return keyValue;
				}
				
				if(!keys.isEmpty()){
					for(String s : k){
						if(!keys.contains(s)){
							return keyValue;
						}
					}
				}else{
					keys.addAll(k);
				}
				condition.remove(c);
			}else if(KeyValueStoring.fileExists(
					new File(Config.KEY_VALUE_PATH_IF + 
							databaseName + "/" + tableName, c.get(0)))){
				this.setPath(Config.KEY_VALUE_PATH_IF + databaseName + "/" + tableName, attr.getAttrName());
				System.out.println("IndexFile: " + c.get(0));
				List<String> k = searchByKey(c.get(2), c.get(1));
				
				if(k.isEmpty()){
					return keyValue;
				}
				
				if(!keys.isEmpty()){
					for(String s : k){
						if(!keys.contains(s)){
							return keyValue;
						}
					}
				}else{
					keys.addAll(k);
				}	
				condition.remove(c);
			}
		}
		
		if(keys.isEmpty()){
			this.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
			return fullTableScann(attrList, condition);
		}else{
			this.setPath(Config.KEY_VALUE_PATH_PK + databaseName, tableName);
			Collections.sort(keys);
			return scannAndSelect(attrList, condition, keys);
		}
		
	}
	
	public List<String> searchByKey(String searchKey, String condition){
		
		Cursor cursor = null;
		List<String> primaryKeys = new ArrayList<String>();
		
		try {
			
		    DatabaseEntry theKey = new DatabaseEntry(searchKey.getBytes("UTF-8"));
		    DatabaseEntry theData = new DatabaseEntry();

		    cursor = testDB.openCursor(null, null);
		    
		    OperationStatus retVal = cursor.getSearchKeyRange(theKey, theData, 
		                                                 LockMode.DEFAULT);
		    if(condition.equals("==")){
		    	String keyString = new String(theKey.getData(), "UTF-8");
	            String dataString = new String(theData.getData(), "UTF-8");
	            String separator = keyString.charAt(keyString.length()-1) + "";
				String data = dataString.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(data.split(", ")));
	            return dataList;
		    }
		    
		    if(condition.contains("=")){
		    	String keyString = new String(theKey.getData(), "UTF-8");
	            String dataString = new String(theData.getData(), "UTF-8");
	            String separator = dataString.charAt(dataString.length()-1) + "";
				String data = dataString.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(data.split(", ")));
	            primaryKeys.addAll(dataList);
	            
	            if(condition.equals(">=")){
					retVal = cursor.getNext(theKey, theData, LockMode.DEFAULT);
	            }else if(condition.equals("<=")){
	            	retVal = cursor.getPrev(theKey, theData, LockMode.DEFAULT);
	            }
		    }
	        while (retVal == OperationStatus.SUCCESS) {
	        	
	            String keyString = new String(theKey.getData(), "UTF-8");
	            String dataString = new String(theData.getData(), "UTF-8");
	            String separator = dataString.charAt(dataString.length()-1) + "";
				String data = dataString.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(data.split(", ")));
	            
				if(condition.equals(">=")){
					retVal = cursor.getNext(theKey, theData, LockMode.DEFAULT);
					primaryKeys.addAll(dataList);
	            }else if(condition.equals("<=")){
	            	retVal = cursor.getPrev(theKey, theData, LockMode.DEFAULT);
	            	primaryKeys.addAll(dataList);
	            }else if(condition.equals(">")){
	            	retVal = cursor.getNext(theKey, theData, LockMode.DEFAULT);
	            	primaryKeys.addAll(dataList);
	            }else if(condition.equals("<")){
	            	retVal = cursor.getPrev(theKey, theData, LockMode.DEFAULT);
	            	primaryKeys.addAll(dataList);
	            }
	        }
		    
		} catch (Exception e) {
			System.out.println("Select by id faild! " + e.getMessage());
			throw new DatabaseException("Faild, pleas try again later!");
		} finally {
		   try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.out.println("Select by id close faild! " + e.getMessage());
				throw new DatabaseException("Faild, pleas try again later!");
			}
		}
		return primaryKeys;
	}
	
	public Map<String, List<String>> fullTableScann(List<String> attrList, List<List<String>> condition)throws DatabaseException{
		Cursor cursor = null;
		Map<String, List<String>> keyValue = null;

		try {
			cursor = testDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			keyValue = new HashMap<String, List<String>>();
			
			while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				String key = new String(foundKey.getData());
				String data = new String(foundData.getData());
				String separator = data.charAt(data.length()-1) + "";
				String keyString = key.replace(separator, "");
				String dataString = data.replace(separator, "").replace("[", "").replace("]", "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(dataString.split(", ")));
				boolean ok = true;
				for(int i = 0; i < condition.size(); i++){
					List<String> c = condition.get(i);
					int index = attrList.indexOf(c.get(0));
					if(index == 0){
						int num;
						int num2;
						switch(c.get(1)){
							case "==":
								if(!keyString.equals(c.get(2))){
									ok = false;
								}
							break;
							case ">=":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(keyString);
								if(num2 < num){
									ok = false;
								}
							break;
							case "<=":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(keyString);
								if(num2 > num){
									ok = false;
								}
							break;
							case "!=":
								if(keyString.equals(c.get(2))){
									ok = false;
								}
							break;
							case ">":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(keyString);
								if(num2 <= num){
									ok = false;
								}
							break;
							case "<":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(keyString);
								if(num2 >= num){
									ok = false;
								}
							break;
						}
					}else if(dataList.size() >= index){
						int num;
						int num2;
						switch(c.get(1)){
							case "==":
								if(!dataList.get(index-1).equals(c.get(2))){
									ok = false;
								}
							break;
							case ">=":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(dataList.get(index-1));
								if(num2 < num){
									ok = false;
								}
							break;
							case "<=":
								num = Integer.parseInt(c.get(2));
								num2 = Integer.parseInt(dataList.get(index-1));
								if(num2 > num){
									ok = false;
								}
							break;
							case "!=":
								if(dataList.get(index-1).equals(c.get(2))){
									ok = false;
								}
							break;
						}
					}else{
						ok = false;
					}
				}
				if(ok){
					keyValue.put(keyString, dataList);
				}
			}
			
		} catch (com.sleepycat.je.DatabaseException de) {
			System.err.println("Error accessing database." + de);
			throw new DatabaseException("Error accessing database, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				e.printStackTrace();
			}
		}

		return keyValue;
	}

	public Map<String, List<String>> scannAndSelect(List<String> attrList, List<List<String>> condition,
																List<String> keys)throws DatabaseException{
		Cursor cursor = null;
		Map<String, List<String>> keyValue = null;

		try {
			
			keyValue = new HashMap<String, List<String>>();			
			DatabaseEntry theKey = new DatabaseEntry(keys.get(0).getBytes("UTF-8"));
		    DatabaseEntry theData = new DatabaseEntry();
		    cursor = testDB.openCursor(null, null);
		    OperationStatus retVal = cursor.getSearchKeyRange(theKey, theData, LockMode.DEFAULT);
		    int indexKeys = 0;
		    
		    while (retVal == OperationStatus.SUCCESS  && indexKeys < keys.size()) {
	        	
	            String keyString = new String(theKey.getData(), "UTF-8");
	            String dataString = new String(theData.getData(), "UTF-8");
	            String separator = dataString.charAt(dataString.length()-1) + "";
				String data = dataString.replace(separator, "").replace("[", "").replace("]", "");
				String key = keyString.replace(separator, "");
				List<String> dataList = new ArrayList<String>(Arrays.asList(data.split(", ")));
				
				if(key.equals(keys.get(indexKeys))){
					boolean ok = true;
					indexKeys++;
					
					for(int i = 0; i < condition.size(); i++){
						List<String> c = condition.get(i);
						int index = attrList.indexOf(c.get(0));
						
						if(index == 0){
							int num;
							int num2;
							switch(c.get(1)){
								case "==":
									if(!key.equals(c.get(2))){
										ok = false;
									}
								break;
								case ">=":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(key);
									if(num2 < num){
										ok = false;
									}
								break;
								case "<=":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(key);
									if(num2 > num){
										ok = false;
									}
								break;
								case "!=":
									if(key.equals(c.get(2))){
										ok = false;
									}
								break;
								case ">":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(key);
									if(num2 <= num){
										ok = false;
									}
								break;
								case "<":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(key);
									if(num2 >= num){
										ok = false;
									}
								break;
							}
							
						}else if(dataList.size() >= index){
							int num;
							int num2;
							switch(c.get(1)){
								case "==":
									if(!dataList.get(index-1).equals(c.get(2))){
										ok = false;
									}
								break;
								case ">=":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(dataList.get(index-1));
									if(num2 < num){
										ok = false;
									}
								break;
								case "<=":
									num = Integer.parseInt(c.get(2));
									num2 = Integer.parseInt(dataList.get(index-1));
									if(num2 > num){
										ok = false;
									}
								break;
								case "!=":
									if(dataList.get(index-1).equals(c.get(2))){
										ok = false;
									}
								break;
							}
						}else{
							ok = false;
						}
					}
					if(ok){
						keyValue.put(key, dataList);
					}
					retVal = cursor.getNext(theKey, theData, LockMode.DEFAULT);
				}
			}
			
		} catch (com.sleepycat.je.DatabaseException | UnsupportedEncodingException de) {
			System.err.println("Full table scan select failed." + de.getMessage());
			throw new DatabaseException("Select failed, try again later!");
		} finally {
			try {
				cursor.close();
				testDB.close();
				dbEnv.close();
			} catch (com.sleepycat.je.DatabaseException e) {
				System.err.println("Full table scan select failed." + e.getMessage());
				throw new DatabaseException("Select failed, try again later!");
			}
		}

		return keyValue;
	}
	
	public void close(){
		try {
			testDB.close();
			dbEnv.close();
		} catch (com.sleepycat.je.DatabaseException e) {
			throw new DatabaseException("Close failed!");
		}
		
	}
	
}
