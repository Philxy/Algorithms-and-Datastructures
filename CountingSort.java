
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class CountingSort {

    static int[] prefixes;

    /**
     * Erzeugt eine Person mit Name, Alter, Bundesstaat, Geschlecht und ID.
     */
    private static class Person {

        private String name;
        private String state;
        private char sex;
        private int age;
        private int ID;

        /**
         * Übernahme aus der Vorlage
         * @param name
         * @param state
         * @param sex
         * @param age
         * @param ID
         */
        public Person(String name, String state, char sex, int age, int ID) {
            this.name = name;
            this.state = state;
            this.sex = sex;
            this.age = age;
            this.ID = ID;
        }

        /**
         * Gibt die ID zurück. (Übernahme aus Vorlage)
         * @return
         */
        public int getID() {
            return ID;
        }

        /**
         * @return string representation of a person including all the data
         */
        public String toString() {
            String record = name + " " + state + " " + sex + " " + age + " " + ID;
            return record;
        }
    }


    /**
     * Funktion zum Einlesen eines Files (Übernahme aus Vorlage)
     *
     * @param fileName
     * @return
     */
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


    /**
     *
     * Funktion zum Schreiben von Personendaten in einen File
     * 	k spezifiziert bis zum wievielten Element die Daten in das File geschrieben werden
     * 	(Übernahme aus Vorlage)
     *
     * @param fileName
     * @param data
     * @param k
     */
    public static void write(String fileName, ArrayList<Person> data, int k) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (int i = 0; i < k; i++)
                writer.write(data.get(i).toString() +"\n");

            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zählt wie oft eine ID vorkommt und scheibt diese Zahl in die ArrayList C an die Stelle ID-1.
     * Gibt C zurück.
     * @param persons
     * @return C Array
     */
    public static int[] count(ArrayList<Person> persons) {
        int[] C = new int[100000];
        for(int j: C){
            C[j]=0;
        }
       for(Person p: persons) {
           C[p.getID() - 1] += 1;
       }
       return C;
    }

    /**
     *  Erstellt hier das Präfixsummenarray. Dies passiert indem man immer den jetzigen Wert aus dem Array mit den davor addiert.
     *  Das Präfixsummenarray wird dann in prefix abgespeichert, damit man dieses später in der Datei prefixes.txt ausgeben kann. 
        Dabei handelt es sich um das bearbeitete Array!
     * @param A
     * @return A Array
     */
    public static int[] add(int[] A) {
        for(int i = A.length-2; i>-1; i--) {
            A[i] = A[i+1] + A[i];
        }
        prefixes = A;
        return A;
    }

    /**
     * Given an array list of Persons, this method implements the counting sort algorithm
     * sorting the citizens by their ID's (descending).
     * @param A
     * @return sorted citizens as array of type Person
     */
    public static Person[] sort(ArrayList<Person> A) {
        int[] C = add(count(A));
        Person[] R = new Person[A.size()];
        for(int i = A.size()-1; i>=0; i--) {
            R[C[A.get(i).getID()-1]-1] = A.get(i);
            C[A.get(i).getID()-1] -= 1;
        }
        return R;
    }

    /**
     * Converts the information of an american citizen given as a string into an object of
     * class "Person". The result is being returned as an array list.
     * @param S string list containing information about people
     * @return array list consisting of persons which are instances of class "Person"
     */
    public static ArrayList<Person> getPersons( ArrayList<String> S) {
        ArrayList<Person> persons = new ArrayList<>();
        for(String s: S) {
            String[] sp = s.split("\\s+");
            persons.add(new Person( sp[0], sp[1], sp[2].charAt(0),  Integer.parseInt(sp[3]), Integer.parseInt(sp[4])));
        }
        return persons;
    }

    /**
     * Writes the content of an int array to a file (line by line).
     * @param fileName
     * @param data
     */
    public static void write(String fileName, int[] data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (int i = 0; i < data.length; i++)
                writer.write(data[i]+"\n");

            writer.close();
            System.out.println("... " + data.length + " data lines written in file " + fileName + ".");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        if(args.length == 2) {
            ArrayList<String> citizens = new ArrayList<>();
            citizens = read(args[0]);
            ArrayList<Person> persons = new ArrayList<>();
            Person[] sorted = sort(getPersons(citizens));
            write(args[1], new ArrayList<Person>(Arrays.asList(sorted)), sorted.length);
            write("prefixes.txt", prefixes);
        } else {
            System.out.println("Expecting two files as arguments!");
            System.exit(0);
        }


    }

}
