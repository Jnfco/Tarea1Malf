/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;


import java.util.Scanner;
import static javaapplication5.Thompson.compile;
public class Main
{
   

   public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
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
            if (line.equals(":q") || line.equals("QUIT"))
                break;
            AFND nfa_of_input = compile(line);
            //System.out.println("\nNFA:");
            
            
            nfa_of_input.display();
            
            if(nfa_of_input.transitions.size() == 1)
            {
                AFD afd = new AFD(nfa_of_input, line.charAt(0));
            }
            else
            {
                AFD afd = new AFD(nfa_of_input);
            }
            
            
        }
    }
   
}
