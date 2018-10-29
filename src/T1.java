
package javaapplication5;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import static javaapplication5.Thompson.compile;
public class Main
{
   

   public static void main(String[] args) throws FileNotFoundException, IOException{
        /*Scanner sc = new Scanner(System.in);
        String line;
        System.out.println("\nEnter a regular expression with the " +
            "alphabet ['a','z'] & E for empty "+"\n* for Kleene Star" + 
            "\nelements with nothing between them indicates " +
            "concatenation "+ "\n| for Union \n\":q\" to quit");
        while(sc.hasNextLine()){
            System.out.println("Enter a regular expression with the " +
                "alphabet ['a','z'] & E for empty "+"\n* for Kleene Star" + 
                "\nelements with nothing between them indicates " +
                "concatenation "+ "\n| for Union \n\":q\" to quit");
            line = sc.nextLine();
         */
       String line;
       
       BufferedReader br = new BufferedReader(new FileReader("test.in"));
       
        line = br.readLine();
        AFND afnd = compile(line);
            
            
            afnd.display();
            
            if(afnd.transitions.size() == 1)
            {
                AFD afd = new AFD(afnd, line.charAt(0));
            }
            else
            {
                AFD afd = new AFD(afnd);
            }
            
            
        }
    }
   

