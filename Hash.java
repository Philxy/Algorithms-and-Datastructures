import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class Hash {

	//Funktion zum Einlesen eines Files
	public static ArrayList<String> read(String fileName) {

		ArrayList<String> data = new ArrayList<>();

		try {		
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String line = "";

			while (line != null) {

				line = reader.readLine();

				if (line != null) {
					data.add(line);
				}

			}
			reader.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	//Funktion zum Schreiben von Personendaten in einen File
	public static void write(String fileName, ArrayList<?> data) {

		try {		
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			for (int i = 0; i < data.size(); i++)
				writer.write(data.get(i).toString() +"\n");

			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Klasse Person
	private static class Person {

		private String name;
		private String state;
		private char gender;
		private int age;
		private int ID;
		private boolean deleted = false;
	
		//Constructor
		public Person(String name, String state, char gender, int age, int ID) {
			this.name = name;
			this.state = state;
			this.gender = gender;
			this.age = age;
			this.ID = ID;
		}

		public Person() {
		}

		public void setID(int id) {
			this.ID = id;
		}

		public String getName() {
			return name;
		}

		public int getID() {
			return ID;
		}

		public boolean isDeleted() {
			return deleted;
		}

		public void delete() {
			this.deleted = true;
		}

		public boolean equals(Person other) {
			if(name == null) {
				if(other.getName() == null) {
					return true;
				} else {
					return false;
				}
			}

			if (!name.equals(other.getName()))
				return false;

			if (ID != other.getID())
				return false;

			return true;
		}

		public String toString() {
			String record = name + " " + state + " " + gender + " " + age + " " + ID;

			if (ID == -1)
				record = "-1";

			return record;
		}

	}

	/**
	 * Diese Klasse implementiert eine Hash Tabelle mit offener Adressierung mit linearer Sondierung.
	 * Für eine volle HashTabelle werden weitere Elemente einer ArrayListe angehängt.
	 */
	private static class HashTable {

		private ArrayList < Person > table;
		private ArrayList < Person > overflow = new ArrayList<>();

		int m;
		int c;

		public HashTable(int m, int c) {
			this.m = m;
			this.c = c;
			table = new ArrayList< Person >();
			for (int i = 0; i < m; i++) {
				table.add(new Person());
			}
		}

		public ArrayList< Person > getTable() {
			return table;
		}

		public ArrayList< Person > getOverflow() {
			return overflow;
		}

		private int getHash(int ID) {
			int m = table.size();
			int h = ID % m;
			return h;
		}

		/**
		 * Fügt eine Person in die Hash Tabelle ein falls ein freier (oder bereits gelöschter) Eintrag
		 * gefunden wird. Falls das lineare Sondieren keinen freien Eintrag findet wird das Element
		 * einer zusätzlichen ArrayListe angehängt.
		 * @param p
		 */
		public void insert(Person p) {
			int ID = p.getID();
			int h = getHash(ID);
			for(int i = 0; i < m/c+1; i++){
				int pos = getHash(h+i*c);
				if(table.get(pos).equals(new Person()) || table.get(pos).isDeleted()) {
					table.set(getHash(h+i*c), p);
					return;
				}
			}
			overflow.add(p);
			return;
		}

		/**
		 * Diese Methode löscht eine Person aus der Hashtablle. Diese Person wird allerdings nicht ganz gelöscht, es wird lediglich
		 * die ID auf -1 gesetzt und dies gilt dann als gelöscht.
		 * @param p, Person
		 */
		public void delete(Person p) {
			int ID = p.getID();
			int h = getHash(ID);
			for(int i = 0; i < m/c +1; i++){
				if (table.get(getHash(h+i*c)).equals(p)) {
					table.get(getHash(h+i*c)).setID(-1);
					return;
				}
			}
			overflow.remove(p);
		}

		/**
		 * Diese Methode sucht nach der ersten Übereinstimmenden ID und gibt dann diese Person zurück. Zuerst wird in der
		 * Hashtabelle gesucht, danach in der Zusatzdatenstrktur. Wenn die ID nicht gefunden wurde, wird null zurückgegeben.
		 * @param ID
		 * @return null oder Person
		 */
		public Person find(int ID) {
			int h = getHash(ID);
			for(int i = 0; i < m/c+1; i++){
				if (table.get(getHash(h+i*c)).getID() == ID) {
					return table.get(getHash(h+i*c));
				}
			}
			for(int i = 0; i<overflow.size(); i++) {
				if(overflow.get(i).getID() == ID) {
					return overflow.get(i);
				}
			}

			//return new Person("-1", " ", " ", " ",-1 );
			return null; // falls ID nicht enthalten
		}
	}

	//Comparator zum Sortieren von Personen nach ID
	private static class PersonByIDComparator implements Comparator<Person> {
		public int compare(Person p1, Person p2) {
			return Integer.compare(p2.getID(), p1.getID());
		}
	}

	

	public static void main(String args[]) {


		if (args.length < 6)
			return;

		//Parameter für Lineares Sondieren
		int c = Integer.parseInt(args[0]);

		//Größe der Hashtabelle
		int m = Integer.parseInt(args[1]);

		System.out.println(c + " " + m);

		//Eingabedatei1
		String fileName1 = args[2];

		//Datenstruktur zum Verwalten der eingelesenen Elemente
		ArrayList<String> data = read(fileName1);

		//Personendaten
		ArrayList<Person> persons = new ArrayList<>();

		//Konvertierung der eingelesenen Zeichenketten in Personenobjekte
		for (int i = 0; i < data.size(); i++) {

			String[] vpr = null;
			vpr = data.get(i).split("\\s+");

			String name, state;
			char gender;
			int age, ID;

			name = vpr[0];
			state = vpr[1];
			gender = vpr[2].charAt(0);
			age = Integer.parseInt(vpr[3]);
			ID = Integer.parseInt(vpr[4]);

			Person person = new Person(name, state, gender, age, ID);
			persons.add(person);
		}
		
		// neue Hashtabelle
		HashTable ht = new HashTable(m, c);

		//fügt die Personen in die Hashtabelle ein
		for (int i = 0; i < persons.size(); i++) {
			ht.insert(persons.get(i));
		}

		//Ausgabedatei1 gibt die Einträge in der Hashtabelle aus und danach noch die Anzahl der Elemente in der Zusatzdatenstruktur
		ArrayList<String> ausgabe1 = new ArrayList<>();
		for(Person p: ht.getTable()) {
			if(p.isDeleted()) {
				ausgabe1.add("");
			} else {
				ausgabe1.add(p.toString());
			}
		}
		ausgabe1.add(String.valueOf(ht.getOverflow().size()));


		//Löschen jeder zweiten Person
		for (int i = 1; i < persons.size(); i+=2) {
			ht.delete(persons.get(i));
		}


		//Eingabedatei2 mit den IDs
		String fileName2 = args[3];

		ArrayList<String> IDdata = read(fileName2);
		ArrayList<Integer> IDs = new ArrayList<>();
		
		for (int i = 0; i < IDdata.size(); i++) {
			IDs.add(Integer.parseInt(IDdata.get(i)));
		}

		//Suche nach die erste übereinstimmende ID und gibt dazu die passende Person aus
		ArrayList<String> ausgabe2 = new ArrayList<>();
		for(int i = 0; i < IDs.size(); i++) {
			Person p = ht.find(IDs.get(i));
			if (p == null) {
				ausgabe2.add("-1");
			}
			else {
				ausgabe2.add(p.toString());
			}
		}
		write(args[4], ausgabe1);
		write(args[5], ausgabe2);
	}

}
