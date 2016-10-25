package backend;


public class Server {

	public static void main(String[] args) {
		Database db = new Database("D://database.xml");
		//db.createXMLFile();
		//db.createDatabase("test4");
		//db.dropDatabase("test4");
		//db.creatTable("test", "testTable");
		//db.creatAttribute("testTable", "testAttr", "char", "64", "0");
		db.dropTable("testTable");
	}

}
