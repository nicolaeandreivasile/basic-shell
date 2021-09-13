/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author nick
 */
public class Main {

    public static Directory root = new Directory("/");
    public static Directory currDirectory = root;

    public static BufferedReader readFile = null;       //declare input file reader
    public static BufferedWriter writeOutput = null;    //declare output file writer
    public static BufferedWriter writeError = null;     //declare error file writer

    public static void main(String[] args) {

        root.add(root);     // . directory
        root.add(root);     // .. directory

        /* read from file */
        try {
            readFile = new BufferedReader(new FileReader(args[0]));     //initialize input file reader
            writeOutput = new BufferedWriter(new FileWriter(args[1])); //initialize output file writer
            writeError = new BufferedWriter(new FileWriter(args[2]));  //initialize error file writer

            String line;    //declare line 
            int index = 0;

            /* read from input file line by line and execute given command */
            while ((line = readFile.readLine()) != null) {

                index++;
                writeOutput.write(Integer.toString(index));
                writeOutput.write("\n");
                writeError.write(Integer.toString(index));
                writeError.write("\n");
                CommandFactory commandFactory = new CommandFactory();
                BashCommand command = new BashCommand(commandFactory.getCommand(line));
                command.doCommand();

            }

            readFile.close();       //close input file reader
            writeOutput.close();    //close output file writer
            writeError.close();     //close error file writer
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
