package blatt0;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Read {

    public static void main(String[] args) throws IOException {
        
        int k = Integer.parseInt(args[2]);
        final File file = new File( args[0]);
        final File writeTo = new File(args[1]);
        write(getContent(file), writeTo, k);

    }

    /**
     * Returns a list containing all the lines of a given text file.
     * @param file
     * @return
     * @throws IOException
     */
    private static List<String> getContent(File file) throws IOException {
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
     * This method writes the first k entries of a list to a given file.  
     * @param content
     * @param file
     * @param k
     */
    private static void write(List<String> content, File file, int k) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));) {

			for (int i = 0; i<k;i++) {
				bw.write(content.get(i));
				bw.newLine();
			}
			bw.close();

		} catch (IOException e) {
            e.printStackTrace();
		}
       
    }
    
}



