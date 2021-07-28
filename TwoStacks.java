import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;


/**
 * Implements two stack datastructures within one array. The stack operations of the two stack array
 * can be used to create a queue datastructure. 
 * 
 * @author Philipp Rieger and Annika Treutle
 */
public class TwoStacks {    

    

public static class ArrayStacks<T> {


    
    private T[] array;
    private int size;
    int topIndex1, topIndex2, size1, size2;


    /**
     * Erstellt Array welcher zwei Stacks beihalten soll.  
     * Stack 1 sei links im Array. Index beginnt beim linken Ende des Arrays.
     * Stack 2 sei links im Array. Index beginnt beim rechten Ende des Arrays.
     * 
     * @param size maximum size of both stack sizes combined
     */
    public ArrayStacks(int size) {
        this.array =  (T[]) new Object[size];
        topIndex1 = -1;
        topIndex2 = size;
        size1 = 0;
        size2 = 0;
        this.size = size;
    }

    /**
     * 
     * @param t setzt dieses Element auf Top von Stack 1
     */
    public void push1(T t) {
        
        if(size1 + size2 > size) {
            System.out.println("Stack is full.");
            System.exit(0);
        } else {
            size1++;
            topIndex1++;
            array[topIndex1] = t;
            
        }
        
    }

    /**
     * 
     * @param t  setzt dieses Element auf Top von Stack 1
     */
    public void push2(T t) {
        
        if(size1 + size2 > size) {
            System.out.println("Stack is full.");
            System.exit(0);
        } else {
            size2++;
            topIndex2--;
            array[topIndex2] = t;
            
        }
        
    }

    /**
     * Entfernt oberstes Element von Stack 1
     */
    public void pop1() {
        if(size1    != 0) {
            array[topIndex1] = null;
            topIndex1--;
            size1--;
        } else {
            System.out.println("Stack1 is empty. Cannot pop.");
            System.exit(0);
        }
       
    }
    
    /**
     * Entfernt oberstes Element von Stack 2
     */
    public void pop2() {
        if(size2    != 0) {
            array[topIndex2] = null;
            topIndex2++;
            size2--;
        } else {
            System.out.println("Stack2 is empty. Cannot pop.");
            System.exit(0);
        }
       
    }
    
    /**
     * 
     * @return oberstes Element von Stapel 1
     */
    public  T top1() {
        if(size1 == 0) {
            System.out.println("Stack1 is empty. Cannot return top.");
            System.exit(0);
        } 
        return  array[topIndex1];
    }


    /**
     * 
     * @return oberstes Element von Stapel 2 
     */
    public  T top2() {
        if(size2 == 0) {
            
            System.out.println("Stack2 is empty. Cannot return top.");
            System.exit(0);
        } 
        return  array[topIndex2];
    }

    
    
    
    /**
     * 
     * @param t
     */
    public void newpush(T t) {
        push1(t);
        // Falls Stack2 leer kann nicht verglichen werden. Darum wird direkt push2(t) ausgeführt.
        if(size2 == 0) {
            push2(t);
            return;
        }
        if((new Compare()).compare(top1(), top2()) >= 0) {
            push2(t);
        }
    }

    
    public void newpop() {
        if((new Compare()).compare(top1(),  top2()) == 0) {
            pop2();
        }
        pop1();
    }


    /**
     * 
     * @return aximum vom rechten Stack
     */
    public T max() {
        return top2();
    }
    

    public void enqueue(T t) {
        // move left stack on top of right stack
        while (size1 != 0) {  
            push2(top1());
            pop1();
        }
        // move right stack back onto the left stack
        push1(t);
        while (size2 != 0) {  
            push1(top2());
            pop2();
        }
    }

    
    public void dequeue() {
        pop1();
    }

    /**
     * 
     * @return Head of queue
     */
    public T front() {
        return top1();
    }
    
    
    
    /**
     *  Generischer Comparator um Strings bzw. Integer zw vergleichen
    */
    public class Compare implements Comparator<T> {
        /** 
         *  
         *  Vergleicht zwei Strings nach der Häufigkeit des Buchstaben a bzw. A und Integer nach ihrer Größe.
         *  Integer werden nach ihrer größe verglichen. 
         * 
         *  @o1 zu vergleichende Elemente
         *  @o2         ""
         * 
         *  @return 0 für gleiche Werte, 1 für o1 > o2 und 0 für o2 > o1
         */
        public int compare(T o1, T o2) {
            if(o1 instanceof String) {
                int a1 = 0, a2 = 0;
                for(int i = 0; i<((String) o1).length();i++) {
                    if(Character.toLowerCase(((String) o1).charAt(i)) == 'a') {
                        a1++;
                    }
                }
                for(int i = 0; i<((String) o2).length();i++) {
                    if(Character.toLowerCase(((String) o2).charAt(i)) == 'a') {
                        a2 ++;
                    }
                }
                return Integer.compare(a1, a2);
            }
            if(o1 instanceof Integer) {
                return Integer.compare((Integer)o1, (Integer)o2);
            }
            return 0;
        }
    
    }

    
}




    /**
     * Returns a String list containing all the lines of a given text file.
     * @param file
     * @return
     * @throws IOException
     */
    private static List<String> getStrings(File file) throws IOException {
        List<String> content = new LinkedList<String>();
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                content.add(line);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }


 
    /**
     * Returns a Integer list containing all the lines of a given text file.
     * @param file
     * @return
     * @throws IOException
     */
    private static List<Integer> getInteger(File file) throws IOException {
        List<Integer> content = new LinkedList<Integer>();
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null) {
                content.add(Integer.parseInt(line));
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    


    
    /**
     * Schreibt die Einträge einer  ArrayListe Zeile für Zeile in eine Textdatei.
     * @param fileName
     * @param data
     */
	public static void write(String fileName, ArrayList<?> data) {

		try {		
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			for (int i = 0; i < data.size(); i++)
				writer.write(data.get(i)+"\n");

			writer.close();
			System.out.println("... " + data.size() + " data lines written in file " + fileName + ".");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Diese Methode führt ausgehend von einer Liste aus Integers oder Strings die Operationen newpop() oder newpush()
     * in einer vorgegebenen Reihenfolge aus. Diese beiden Operationen wirken auf zwei Stacks, welche in einem
     * einzigen Array gespeichert werden.
     * Dabei gelangt immer das aktuelle Maximum einer Liste hinzugefügt. Nach dem Abarbeiten 
     * der Operationen wird diese Liste ausgegeben.
     *  
     * 
     * @param <T>   Datentyp entweder String oder Integer
     * @param input Liste von Integers oder Strings die es zu sortieren gilt
     * @param operations Operationen die am Input ausgeführt werden
     * @return
     */
    private static <T> ArrayList<T> runStacks(List<T> input, List<Integer> operations) {
        ArrayStacks<T> as = new ArrayStacks<>(2*operations.size());
        ArrayList<T> maximums = new ArrayList<>();
        int ID = 0;
        for(int i = 0; i<operations.size();i++) {
            if(Integer.valueOf(operations.get(i)) == 1) {
                as.newpush(input.get(ID));
                ID++;
            } else {
                as.newpop();
            }
            maximums.add(as.max());
        }
        return maximums;
    }
    
    /**
     * This method takes an input as a list and executes queue operations on the list's entries. 
     * This is being done by creating a the two stack datastruckture in an array simulating a queue.
     * Returns a list which contains the front after every operation on the entry set.
     * 
     * @param <T>
     * @param input input file with data
     * @param operations  operations on the input data
     * @return ArrayList a list containing the front of the queue after each operation
     */
    private static<T> ArrayList<T> runQueue(List<T> input, List<Integer> operations) {
        ArrayStacks<T> as = new ArrayStacks<>(2*operations.size());
        ArrayList<T> fronts = new ArrayList<>();
        int ID = 0;
        for(int i = 0; i<operations.size();i++) {
            if(Integer.valueOf(operations.get(i)) == 1) {
                as.enqueue(input.get(ID));
                ID++;
            } else {
                as.dequeue();
            }
            fronts.add(as.front());
        }
        return fronts;
    }
    
    public static void main(String[] args) throws IOException {
        
        final String c = args[4];
        final File inputFile = new File(args[0]);
        final File operationFile = new File(args[1]);
        
        // Falls Eingabe aus Strings besteht
        if(c.equals("s")) {
            List<String> content = getStrings(inputFile);
            List<Integer> operations = getInteger(operationFile);
            write(args[2],runStacks(content, operations));
            write(args[3], runQueue(content, operations));
        }
        // Falls Eingabe aus Integers besteht
        if(c.equals("i")) {
            List<Integer> content = getInteger(inputFile);
            List<Integer> operations = getInteger(operationFile);
            write(args[2],runStacks(content, operations));
            write(args[3], runQueue(content, operations));
        }
        
    } 
}