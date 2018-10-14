
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

public class Thompson{
    /*
        Trans - object is used as a tuple of 3 items to depict transitions
            (state from, symbol of tranistion path, state to)
    */
    public static class Trans{
        private int state_from; // Estado inicial
        private int state_to; // Estado final
        private char trans_symbol; // Simbolo de transicion

        public Trans(int v1, int v2, char sym){
            this.state_from = v1;
            this.state_to = v2;
            this.trans_symbol = sym;
        }
    }

    /*
        NFA - serves as the graph that represents the Non-Deterministic
            Finite Automata. Will use this to better combine the states.
    */
    public static class NFA{
        public ArrayList <Integer> states; // conjunto de estados
        public ArrayList <Trans> transitions; // Conjunto de transiciones
        public int final_state; // Estado final del automata
        
        public NFA(){
            this.states = new ArrayList <Integer> (); //Crear el arreglo de enteros para los estados
            this.transitions = new ArrayList <Trans> (); //Crear un arreglo de tipo transicion
            this.final_state = 0;// el estado final inicial es 0
        }
        public NFA(int size){
            this.states = new ArrayList <Integer> ();
            this.transitions = new ArrayList <Trans> ();
            this.final_state = 0;
            this.setStateSize(size);// Asignar la cantidad de estados
        }
        public NFA(char c){
            this.states = new ArrayList<Integer> ();
            this.transitions = new ArrayList <Trans> ();
            this.setStateSize(2);
            this.final_state = 1;
            this.transitions.add(new Trans(0, 1, c));// crea la transicion del 0 al 1 usando el caracter c
        }

        public void setStateSize(int size){ // metodo que llena el arreglo de estados con la cantidad indicada
            for (int i = 0; i < size; i++)
                this.states.add(i);
        }

        public void display(){ // Metodo que imprime las transiciones del automata convertido
            for (Trans t: transitions){
                System.out.println("("+ t.state_from +", "+ t.trans_symbol +
                    ", "+ t.state_to +")");
            }    
        }
    }

    /*
        kleene() - Highest Precedence regular expression operator. Thompson
            algoritm for kleene star.
    */
    public static NFA kleene(NFA n){ //metodo para la estrella de kleene que recibe un objeto tipo NFA
        NFA result = new NFA(n.states.size()+2); // crea un nuevo NFA 
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
    public static NFA concat(NFA n, NFA m){
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
    public static NFA union(NFA n, NFA m){
        NFA result = new NFA(n.states.size() + m.states.size() + 2);

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
    public static boolean alphabet(char c){ return alpha(c) || c == '_' || alphaM(c);}
    public static boolean regexOperator(char c){
        return c == '(' || c == ')' || c == '*' || c == '|';
    }
    public static boolean validRegExChar(char c){
        return alphabet(c) || regexOperator(c);
    }
    // validRegEx() - checks if given string is a valid regular expression.
    public static boolean validRegEx(String regex)
    {
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
                if (regex.charAt(i) == '(') l++;
                if (regex.charAt(i) == ')') r++;
            }
            // revisamos la sintaxis para la operación or
            else if (regex.charAt(i) == '|')
            {
                // revisamos que no hayan caracteres no válidos a la izquierda
                if(i == 0 || regex.charAt(i-1) == '(' || regex.charAt(i-1) == '*') 
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
                if (regex.charAt(i+1) == '|' || regex.charAt(i+1) == '*') {return false;}
            }
        }
        // si la cantidad de paréntesis izq y der son los mismos retornamos true
        // false en caso contrario (la ER no es válida)
        return l == r; 
    }

    /*
        compile() - compile given regualr expression into a NFA using 
            Thompson Construction Algorithm. Will implement typical compiler
            stack model to simplify processing the string. This gives 
            descending precedence to characters on the right.
    */
    public static NFA compile(String regex){
        if (!validRegEx(regex)){
            System.out.println("Invalid Regular Expression Input.");
            return new NFA(); // empty NFA if invalid regex
        }
        
        Stack <Character> operators = new Stack <Character> ();
        Stack <NFA> operands = new Stack <NFA> ();
        Stack <NFA> concat_stack = new Stack <NFA> ();
        boolean ccflag = false; // concat flag
        char op, c; // current character of string
        int para_count = 0;
        NFA nfa1, nfa2;

        for (int i = 0; i < regex.length(); i++){
            c = regex.charAt(i);
            if (alphabet(c)){
                operands.push(new NFA(c));
                if (ccflag){ // concat this w/ previous
                    operators.push('.'); // '.' used to represent concat.
                }
                else
                    ccflag = true;
            }
            else{
                if (c == ')'){
                    ccflag = false;
                    if (para_count == 0){
                        System.out.println("Error: More end paranthesis "+
                            "than beginning paranthesis");
                        System.exit(1);
                    }
                    else{ para_count--;}
                    // process stuff on stack till '('
                    while (!operators.empty() && operators.peek() != '('){
                        op = operators.pop();
                        if (op == '.'){
                            nfa2 = operands.pop();
                            nfa1 = operands.pop();
                            operands.push(concat(nfa1, nfa2));
                        }
                        else if (op == '|'){
                            nfa2 = operands.pop();
                            
                            if(!operators.empty() && 
                                operators.peek() == '.'){
                                
                                concat_stack.push(operands.pop());
                                while (!operators.empty() && 
                                    operators.peek() == '.'){
                                    
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
                }
                else if (c == '*'){
                    operands.push(kleene(operands.pop()));
                    ccflag = true;
                }
                else if (c == '('){ // if any other operator: push
                    operators.push(c);
                    para_count++;
                }
                else if (c == '|'){
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
            NFA nfa_of_input = compile(line);
            System.out.println("\nNFA:");
            nfa_of_input.display();
        }
    }
}