import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class DataBases {

	public static ServerConnection connection = new ServerConnection();

	public static Scanner scan = new Scanner(System.in);
	public static BufferedReader bReader = null;
	public static FileReader fReader = null;

	static Random rand = new Random();
	static String message = "Please type: reportCracker, reportJoke, insertCracker or exit.";

	static String username = "pxp660";
	static String password = "0PSH2sqbTW";
	static String database = "";
	static String URL = "jdbc:postgresql://mod-intro-databases.cs.bham.ac.uk/" + database;

	public static String clean() {

		String drop_Cracker = "DROP TABLE Cracker;\n";
		String drop_Joke = "DROP TABLE Joke;\n";
		String drop_Gift = "DROP TABLE Gift;\n";
		String drop_Hat = "DROP TABLE Hat;\n";

		String clean = drop_Cracker + drop_Gift + drop_Joke + drop_Hat;

		return clean;
	}

	public static String create_tables() {

		String create_joke = "CREATE TABLE Joke(jid INTEGER NOT NULL UNIQUE,\n"
								+ "	joke CHAR(150) UNIQUE,\n" 
								+ "	royalty INTEGER,\n" 
								+ "	PRIMARY KEY (jid), "
								+ "CHECK (jid>=0),"
								+ "CHECK (royalty>=0));\n";
		String create_gift = "CREATE TABLE Gift(gid INTEGER NOT NULL UNIQUE,\n"
								+ "	description CHAR(50) UNIQUE,\n" 
								+ "	price INTEGER,\n" 
								+ "	PRIMARY KEY (gid), "
								+ "CHECK (gid >= 0),"
								+ "CHECK (price >= 0));\n";
		String create_hat = "CREATE TABLE Hat(hid INTEGER NOT NULL UNIQUE,\n"
								+ "	description CHAR(50) UNIQUE,\n" 
								+ "	price INTEGER,\n" 
								+ "	PRIMARY KEY (hid),"
								+ "CHECK (hid >= 0), "
								+ "CHECK (price >= 0));\n";
		String create_cracker = "CREATE TABLE Cracker(cid INTEGER NOT NULL UNIQUE,\n"
								+ "	name CHAR(30), \n" 
								+ "	jid INTEGER NOT NULL,\n" 
								+ "	gid INTEGER NOT NULL,\n"
								+ "	hid INTEGER NOT NULL,\n" 
								+ "	saleprice INTEGER,\n" 
								+ "	quantity INTEGER, \n"
								+ "	PRIMARY KEY (cid),\r\n" 
								+ "	FOREIGN KEY (jid) REFERENCES Joke(jid)\r\n"
								+ "		ON DELETE CASCADE\n" 
								+ "		ON UPDATE CASCADE,\n"
								+ "	FOREIGN KEY (gid) REFERENCES Gift(gid)\r\n" 
								+ "		ON DELETE CASCADE\n"
								+ "		ON UPDATE CASCADE,\n" 
								+ "	FOREIGN KEY (hid) REFERENCES Hat(hid)\r\n"
								+ "		ON DELETE CASCADE\n" 
								+ "		ON UPDATE CASCADE,"
								+ "CHECK (cid >= 0),"
								+ "CHECK (saleprice >= 0),"
								+ "CHECK (quantity >= 0));\n";

		return create_joke + create_gift + create_hat + create_cracker;
	}

	public static String populate() {

		// populate joke
		String jokes = "";
		String gifts = "";
		String hats = "";
		String crackers = "";

		for (int i = 1; i <= 100; i++) {

			int price = rand.nextInt(10) + 1;

			String joke = "'joke " + i + "'";
			jokes = jokes + "INSERT INTO Joke VALUES (" + i + "," + joke + "," + price + ")" + ";" + "\n";
			String gift = "'gift " + i + "'";
			gifts = gifts + "INSERT INTO Gift VALUES (" + i + "," + gift + "," + price + ")" + ";" + "\n";
			String hat = "'hat " + i + "'";
			hats = hats + "INSERT INTO Hat VALUES (" + i + "," + hat + "," + price + ")" + ";" + "\n";

		}

		for (int j = 0; j <= 1000; j++) {

			int joke = rand.nextInt(110) + 1;
			int hat = rand.nextInt(110) + 1;
			int gift = rand.nextInt(110) + 1;
			int saleprice = rand.nextInt(100) + 30;
			int quantity = rand.nextInt(100) + 1;

			String cracker = "'cracker " + j + "'";
			crackers = crackers + "INSERT INTO Cracker VALUES (" + j + "," + cracker + "," + joke + "," + gift + ","
					+ hat + "," + saleprice + "," + quantity + ")" + ";" + "\n";
		}

		return jokes + gifts + hats + crackers;
	}

	public static String populate_real_data(String table, String file) {

		String allEntries = "";
		try {
			fReader = new FileReader(file);

			bReader = new BufferedReader(fReader);

			String currentEntry = bReader.readLine();
			while (currentEntry != null) {
				for (int i = 101; i <= 110; i++) {

					int price = rand.nextInt(10) + 1;

					String cEntry = "'" + currentEntry + "'";

					allEntries = allEntries + "INSERT INTO " + table + " VALUES (" + i + "," + cEntry + "," + price + ")" + ";"
							+ "\n";
					currentEntry = bReader.readLine();
				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException a) {
			System.out.println("IOException");
		}

		return allEntries;

	}

	public static String cracker_report(int id) {

		String get_cracker = " SELECT Cracker.cid, Cracker.name, Gift.description AS \"gdescription\", Joke.joke, Hat.description AS \"hdescription\", Cracker.saleprice, Cracker.quantity, Hat.price AS \"hprice\", Gift.price  \"gprice\", Joke.royalty\r\n"
				+ "FROM Cracker, Gift, Joke, Hat\r\n" + "WHERE Cracker.cid = " + id
				+ " AND Cracker.gid = Gift.gid AND Cracker.hid = Hat.hid AND Cracker.jid = Joke.jid;\r\n";

		return get_cracker;
	}

	public static String joke_report(int id) {

		String get_joke = " SELECT jid, joke, royalty\n" + "FROM Joke\n" + "WHERE jid = " + id + ";\n";

		return get_joke;
	}

	public static String joke_occurence(int id) {

		String get_occurence = "SELECT COUNT(jid)\n" + "FROM Cracker\n" + "WHERE jid = " + id + ";\n";

		return get_occurence;
	}

	public static void cracker_report() {

		System.out.println("Please enter an id.");
		int id_cracker = scan.nextInt();
		ResultSet result = connection.connectToDB(URL, username, password, cracker_report(id_cracker));
		String name = "";
		String gift_description = "";
		String joke = "";
		String hat_description = "";
		int saleprice = 0;
		int hat_price = 0;
		int joke_price = 0;
		int gift_price = 0;
		int quantity = 0;
		try {
			while (result.next()) {
				name = result.getString("name");
				gift_description = result.getString("gdescription");
				joke = result.getString("joke");
				hat_description = result.getString("hdescription");
				saleprice = result.getInt("saleprice");
				hat_price = result.getInt("hprice");
				joke_price = result.getInt("royalty");
				gift_price = result.getInt("gprice");
				quantity = result.getInt("quantity");
			}
			int unit_cost = hat_price + joke_price + gift_price;
			int net_profit = (saleprice - unit_cost) * quantity;
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("ID: " + id_cracker + "\n" + "Name: " + name + "\n" + "Gift Description: "
					+ gift_description + "\n" + "Joke: " + joke + "\n" + "Hat Description: " + hat_description + "\n"
					+ "Sale Price: " + saleprice + "\n" + "Unit Cost: " + unit_cost + "\n" + "Quantity: " + quantity
					+ "\n" + "Net Profit: " + net_profit);
			System.out.println("------------------------------------------------------------------------------");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void joke_report() {
		String joke = "";
		int royalty = 0;
		int occurence = 0;
		System.out.println("Please enter an id.");
		int id_joke = scan.nextInt();
		ResultSet result_joke = connection.connectToDB(URL, username, password, joke_report(id_joke));
		ResultSet j_occ = connection.connectToDB(URL, username, password, joke_occurence(id_joke));
		try {
			while (result_joke.next()) {
				joke = result_joke.getString("joke");
				royalty = result_joke.getInt("royalty");
			}
			while (j_occ.next()) {
				occurence = j_occ.getInt("count");
			}
			int total_royalty = royalty * occurence;
			System.out.println("----------------------------------------------------------");
			System.out.println("Joke ID: " + id_joke + "\n" + "Joke: " + joke + "\n" + "Royalty: " + royalty + "\n"
					+ "No. of uses: " + occurence + "\n" + "Total royalty: " + total_royalty);
			System.out.println("----------------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insert() {
		System.out.println("Please the cracker id, name, jid, gid, hid and sale price separated by: / ");
		String new_cracker = scan.nextLine();
		int new_c_id = Integer.parseInt(new_cracker.split("/")[0]);
		String new_c_name = "'" + new_cracker.split("/")[1] + "'";
		int new_c_jid = Integer.parseInt(new_cracker.split("/")[2]);
		int new_c_gid = Integer.parseInt(new_cracker.split("/")[3]);
		int new_c_hid = Integer.parseInt(new_cracker.split("/")[4]);
		int new_c_sprice = Integer.parseInt(new_cracker.split("/")[5]);
		String insert_cracker = "INSERT INTO Cracker VALUES (" + new_c_id + "," + new_c_name + "," + new_c_jid + ","
				+ new_c_gid + "," + new_c_hid + "," + new_c_sprice + ", 0) ;";

		ResultSet insert = connection.connectToDB(URL, username, password, insert_cracker);

	}

	public static void execute(String line) {

		switch (line) {
		case "reportCracker":
			cracker_report();
			System.out.println(message);
			break;
		case "reportJoke":
			joke_report();
			System.out.println(message);
			break;
		case "insertCracker":
			insert();
			System.out.println(message);
			break;
		}
	}

	public static void main(String[] args) {

		String pathJokes = "C:\\Users\\pavel\\eclipse-workspace\\DataBases\\src\\Jokes";
		String pathHats = "C:\\Users\\pavel\\eclipse-workspace\\DataBases\\src\\Hats";
		String pathGifts = "C:\\Users\\pavel\\eclipse-workspace\\DataBases\\src\\Gifts";

		String ccp = clean() + create_tables() 
				+ populate_real_data("Joke", pathJokes)
				+ populate_real_data("Hat", pathHats) 
				+ populate_real_data("Gift", pathGifts) + populate() 
				;
		connection.connectToDB(URL, username, password, ccp); // cleans the table, creates the tables and populates them

		System.out.println(message);

		String line = scan.nextLine();

		while (!line.equals("exit")) {

			execute(line);

			line = scan.nextLine();

		}
	}

}
