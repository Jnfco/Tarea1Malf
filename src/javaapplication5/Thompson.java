package javaapplication5;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import javaapplication5.AFND;
import javaapplication5.Trans;

public class Thompson {
    
    private static Set<Integer> set1 = new HashSet <Integer> ();
    private static Set<Integer> set2 = new HashSet <Integer> ();
    /*
        Trans - object is used as a tuple of 3 items to depict transitions
            (state from, symbol of tranistion path, state to)
    */


    /*
        NFA - serves as the graph that represents the Non-Deterministic
            Finite Automata. Will use this to better combine the states.
    */


    /*
        kleene() - Highest Precedence regular expression operator. Thompson
            algoritm for kleene star.
    */
    public static AFND kleene(AFND n){ //metodo para la estrella de kleene que recibe un objeto tipo NFA
        AFND result = new AFND(n.states.size()+2); // crea un nuevo NFA 
        result.transitions.add(new Trans(0, 1, '_')); // new trans for q0 ,agrega transicion del 0 al 1 usando el vacio

        // copy existing transisitons
        for (Trans t: n.transitions){
            result.transitions.add(new Trans(t.state_from + 1, //crea mas transiciones apartir de la inicial
            t.state_to + 1, t.trans_symbol));
        }
        
        // add empty transition from final n state to new final state.
        result.transitions.add(new Trans(n.states.size(), 
            n.states.size() + 1, '_')); //Agregar transicion vacio del estado n al nuevo estado final
        
        // Loop back from last state of n to initial state of n.
        result.transitions.add(new Trans(n.states.size(), 1, '_'));

        // Add empty transition from new initial state to new final state.
        result.transitions.add(new Trans(0, n.states.size() + 1, '_'));

        result.final_state = n.states.size() + 1;
        return result;
    }

    /*
        concat() - Thompson algorithm for concatenation. Middle Precedence.
    */
    public static AFND concat(AFND n, AFND m){
        ///*
        m.states.remove(0); // delete m's initial state

        // copy NFA m's transitions to n, and handles connecting n & m
        for (Trans t: m.transitions){
            n.transitions.add(new Trans(t.state_from + n.states.size()-1,
                t.state_to + n.states.size() - 1, t.trans_symbol));
        }

        // take m and combine to n after erasing inital m state
        for (Integer s: m.states){
            n.states.add(s + n.states.size() + 1);
        }
        
        n.final_state = n.states.size() + m.states.size() - 2;
        return n;
        
    }
    
    /*
        union() - Lowest Precedence regular expression operator. Thompson
            algorithm for union (or). 
    */
    public static AFND union(AFND n, AFND m){
        AFND result = new AFND(n.states.size() + m.states.size() + 2);

        // the branching of q0 to beginning of n
        result.transitions.add(new Trans(0, 1, '_'));
        
        // copy existing transisitons of n
        for (Trans t: n.transitions){
            result.transitions.add(new Trans(t.state_from + 1,
            t.state_to + 1, t.trans_symbol));
        }
        
        // transition from last n to final state
        result.transitions.add(new Trans(n.states.size(),
            n.states.size() + m.states.size() + 1, '_'));

        // the branching of q0 to beginning of m
        result.transitions.add(new Trans(0, n.states.size() + 1, '_'));

        // copy existing transisitons of m
        for (Trans t: m.transitions){
            result.transitions.add(new Trans(t.state_from + n.states.size()
                + 1, t.state_to + n.states.size() + 1, t.trans_symbol));
        }
        
        // transition from last m to final state
        result.transitions.add(new Trans(m.states.size() + n.states.size(),
            n.states.size() + m.states.size() + 1, '_'));
       
        // 2 new states and shifted m to avoid repetition of last n & 1st m
        result.final_state = n.states.size() + m.states.size() + 1;
        return result;
    }

   

    // simplify the repeated boolean condition checks
    public static boolean alpha(char c){ return c >= 'a' && c <= 'z';}
    public static boolean alphaM(char c){ return c >= 'A' && c <= 'Z';}
    public static boolean alphaN(char c){ return c >= '0' && c <= '9'; }
    public static boolean alphabet(char c){ return alpha(c) || c == '_' || alphaM(c) || alphaN(c);}
    public static boolean regexOperator(char c){ return c == '(' || c == ')' || c == '*' || c == '|'; }
    public static boolean validAlphabetNoEps(char c){ return alphaN(c) || alphaM(c) || alpha(c); }
    public static boolean validRegExChar(char c){ return alphabet(c) || regexOperator(c); }
    
    public static ArrayList<Character> inputAlphabet;
    // validRegEx() - checks if given string is a valid regular expression.
    public static boolean validRegEx(String regex)
    {
        inputAlphabet = new ArrayList<>();
        inputAlphabet.add('_');
        int l = 0, r = 0;
        // valida que la ER de entrada no sea un string vacío
        if (regex.isEmpty())
            return false;
        // revisamos la ER caracter por caracter
        for (int i = 0; i < regex.length(); i++)
        {
            // validamos que el caracter pertenezca al alfabeto
            if (!validRegExChar(regex.charAt(i)))
            {
                return false;
            }
            // contamos la cantidad de paréntesis derechos e izquierdos
            else if (regex.charAt(i) == '(' || regex.charAt(i) == ')')
            {
                if (regex.charAt(i) == '(' && regex.charAt(i+1) == ')') return false;
                if (regex.charAt(i) == ')' && regex.charAt(i+1) == '(') return false;
                if (regex.charAt(i) == '(') l++;
                if (regex.charAt(i) == ')') r++;
            }
            // revisamos la sintaxis para la operación or
            else if (regex.charAt(i) == '|')
            {
                // revisamos que no hayan caracteres no válidos a la izquierda
                if(i == 0 || regex.charAt(i-1) == '(') 
                {
                    return false;
                }
                // revisamos que no hayan caracteres no válidos a la derecha
                if(i == regex.length() || regex.charAt(i+1) == ')' || 
                        regex.charAt(i+1) == '*' || regex.charAt(i+1) == '|')
                {
                    return false;
                }
            }
            // revisamos la sintaxis de kleene
            else if (regex.charAt(i) == '*' && i < regex.length() - 1)
            {
                // a la izquerda
                if (i == 0 || regex.charAt(i-1) == '(' || regex.charAt(i-1) == '|') {return false;}
                // y a la derecha
                if (regex.charAt(i+1) == '*') {return false;}
            }
            
            if (validAlphabetNoEps(regex.charAt(i)))
            {
                boolean flag = false;
                
                for(int k = 0; k < inputAlphabet.size(); k++)
                {
                    if(inputAlphabet.get(k) == regex.charAt(i)) flag = true;
                }
                if (!flag) inputAlphabet.add(regex.charAt(i));
            }
        }
        // si la cantidad de paréntesis izq y der son los mismos retornamos true
        // false en caso contrario (la ER no es válida)
        /*
        for (int k = 0; k < inputAlphabet.size(); k++)
        {
            System.out.println("alfabeto: "+inputAlphabet.get(k));
        }
        */
        return l == r; 
    }

    /*
        compile() - compile given regualr expression into a NFA using 
            Thompson Construction Algorithm. Will implement typical compiler
            stack model to simplify processing the string. This gives 
            descending precedence to characters on the right.
    */
    public static AFND compile(String regex)
    {
        if (!validRegEx(regex))
        {
            System.out.println("Invalid Regular Expression Input.");
            return new AFND(); // empty NFA if invalid regex
        }
        
        Stack <Character> operators = new Stack <Character> ();
        Stack <AFND> operands = new Stack <AFND> ();
        Stack <AFND> concat_stack = new Stack <AFND> ();
        boolean ccflag = false; // concat flag
        char op, c; // current character of string
        int para_count = 0;
        AFND nfa1, nfa2;

        for (int i = 0; i < regex.length(); i++)
        {
            c = regex.charAt(i);
            if (alphabet(c))
            {
                operands.push(new AFND(c));
                if (ccflag)
                { // concat this w/ previous
                    operators.push('.'); // '.' used to represent concat.
                }
                else
                    ccflag = true;
            }
            else
            {
                if (c == ')')
                {
                    ccflag = false;
                    if (para_count == 0)
                    {
                        System.out.println("Error: More end paranthesis "+
                            "than beginning paranthesis");
                        System.exit(1);
                    }
                    else{ para_count--;}
                    
                    // process stuff on stack till '('
                    while (!operators.empty() && operators.peek() != '(')
                    {
                        op = operators.pop();
                        if (op == '.')
                        {
                            nfa2 = operands.pop();
                            nfa1 = operands.pop();
                            operands.push(concat(nfa1, nfa2));
                        }
                        else if (op == '|')
                        {
                            nfa2 = operands.pop();
                            
                            if(!operators.empty() && operators.peek() == '.')
                            {
                                
                                concat_stack.push(operands.pop());
                                while (!operators.empty() && operators.peek() == '.')
                                {
                                    concat_stack.push(operands.pop());
                                    operators.pop();
                                }
                                nfa1 = concat(concat_stack.pop(), concat_stack.pop());
                                while (concat_stack.size() > 0)
                                {
                                   nfa1 =  concat(nfa1, concat_stack.pop());
                                }
                            }
                            else
                            {
                                nfa1 = operands.pop();
                            }
                            operands.push(union(nfa1, nfa2));
                        }
                    }
                }
                else if (c == '*')
                {
                    operands.push(kleene(operands.pop()));
                    ccflag = true;
                }
                else if (c == '(')
                { // if any other operator: push
                    operators.push(c);
                    para_count++;
                }
                else if (c == '|')
                {
                    operators.push(c);
                    ccflag = false;
                }
            }
        }
        while (operators.size() > 0){
            if (operands.empty()){
                System.out.println("Error: imbalanace in operands and "
                + "operators");
                System.exit(1);
            }
            op = operators.pop();
            if (op == '.'){
                nfa2 = operands.pop();
                nfa1 = operands.pop();
                operands.push(concat(nfa1, nfa2));
            }
            else if (op == '|'){
                nfa2 = operands.pop();
                if( !operators.empty() && operators.peek() == '.'){
                    concat_stack.push(operands.pop());
                    while (!operators.empty() && operators.peek() == '.'){
                        concat_stack.push(operands.pop());
                        operators.pop();
                    }
                    nfa1 = concat(concat_stack.pop(),
                        concat_stack.pop());
                    while (concat_stack.size() > 0){
                       nfa1 =  concat(nfa1, concat_stack.pop());
                    }
                }
                else{
                    nfa1 = operands.pop();
                }
                operands.push(union(nfa1, nfa2));
            }
        }
        return operands.pop();
    }
    
    /*
    public static AFD generateDFA(AFND afnd) 
    {
        // Creating the DFA
        AFD afd = new AFD();

        // Clearing all the states ID for the DFA
        int stateID = 0;

        // Create an arrayList of unprocessed States
        LinkedList <Integer> unprocessed = new LinkedList<Integer> ();

        // Create sets
        set1 = new HashSet <Integer> ();
        set2 = new HashSet <Integer> ();

        // Add first state to the set1
        set1.add(afnd.start_state);

        // Run the first remove Epsilon the get states that
        // run with epsilon
        removeEpsilonTransition();

        // Create the start state of DFA and add to the stack
        State dfaStart = new State (set2, stateID++);

        dfa.getDfa().addLast(dfaStart);
        unprocessed.addLast(dfaStart);

        // While there is elements in the stack
        while (!unprocessed.isEmpty()) {
                // Process and remove last state in stack
                State state = unprocessed.removeLast();

                // Check if input symbol
                for (Character symbol : input) {
                        set1 = new HashSet<State> ();
                        set2 = new HashSet<State> ();

                        moveStates (symbol, state.getStates(), set1);
                        removeEpsilonTransition ();

                        boolean found = false;
                        State st = null;

                        for (int i = 0 ; i < dfa.getDfa().size(); i++) {
                                st = dfa.getDfa().get(i);

                                if (st.getStates().containsAll(set2)) {
                                        found = true;
                                        break;
                                }
                        }

                        // Not in the DFA set, add it
                        if (!found) {
                                State p = new State (set2, stateID++);
                                unprocessed.addLast(p);
                                dfa.getDfa().addLast(p);
                                state.addTransition(p, symbol);

                        // Already in the DFA set
                        } else {
                                state.addTransition(st, symbol);
                        }
                }			
        }
        // Return the complete DFA
        return dfa;
	}

	// Remove the epsilon transition from states
	private static void removeEpsilonTransition() {
		Stack <Integer> stack = new Stack <Integer> ();
		set2 = set1;

		for (Integer st : set1) { stack.push(st); }

		while (!stack.isEmpty()) {
			Integer st = stack.pop();

			ArrayList <Integer> epsilonStates = st.getAllTransitions('_');

			for (State p : epsilonStates) {
				// Check p is in the set otherwise Add
				if (!set2.contains(p)) {
					set2.add(p);
					stack.push(p);
				}				
			}
		}		
	}

	// Move states based on input symbol
	private static void moveStates(Character c, Set<State> states,	Set<State> set) {
		ArrayList <State> temp = new ArrayList<State> ();

		for (State st : states) {	temp.add(st);	}
		for (State st : temp) {			
			ArrayList<State> allStates = st.getAllTransitions(c);

			for (State p : allStates) {	set.add(p);	}
		}
        } 
*/

}