package javaapplication5;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Thompson {
    
    private static Set<Integer> set1 = new HashSet <> ();
    private static Set<Integer> set2 = new HashSet <> ();
    
    public static AFND kleene(AFND n){ //metodo para la estrella de kleene que recibe un objeto tipo NFA
        AFND result = new AFND(n.states.size()+2); // crea un nuevo AFND
        result.transitions.add(new Trans(0, 1, '_')); // agrega transicion del 0 al 1 usando el vacio

        // copia las transiciones existentes
        for (Trans t: n.transitions){
            result.transitions.add(new Trans(t.state_from + 1, //crea mas transiciones apartir de la inicial
            t.state_to + 1, t.trans_symbol));
        }
        
        
        // Agrega transicion vacía desde el estado final n al nuevo estado final
        result.transitions.add(new Trans(n.states.size(), 
            n.states.size() + 1, '_')); 
        
        // Vuelve desde el ultimo estado de n al estado inicial de n
        result.transitions.add(new Trans(n.states.size(), 1, '_'));

        // Agrega una transicion vacia desde eñ nuevp estado inicial al nuevo estado final
        result.transitions.add(new Trans(0, n.states.size() + 1, '_'));

        result.final_state = n.states.size() + 1;
        return result;
    }

    /*
        concat() - Algoritmo de thompson para la concatenación
    */
    public static AFND concat(AFND n, AFND m){
        
        m.states.remove(0); // Elimina los estados iniciales de m

        // Copya las transiciones del AFND m a n
        for (Trans t: m.transitions){
            n.transitions.add(new Trans(t.state_from + n.states.size()-1,
                t.state_to + n.states.size() - 1, t.trans_symbol));
        }

        // Toma m y lo combina con n despues de eliminar el estado inicial de m
        for (Integer s: m.states){
            n.states.add(s + n.states.size() + 1);
        }
        
        n.final_state = n.states.size() + m.states.size() - 2;
        return n;
        
    }
    
    /*
        Algoritmo de thompson para la unión (or)
    */
    public static AFND union(AFND n, AFND m){
        AFND result = new AFND(n.states.size() + m.states.size() + 2);

        
        result.transitions.add(new Trans(0, 1, '_'));
        
        // copia las transiciones existentes de n
        for (Trans t: n.transitions){
            result.transitions.add(new Trans(t.state_from + 1,
            t.state_to + 1, t.trans_symbol));
        }
        
        result.transitions.add(new Trans(n.states.size(),
            n.states.size() + m.states.size() + 1, '_'));

        
        result.transitions.add(new Trans(0, n.states.size() + 1, '_'));

        // copia las transiciones existentes de m
        for (Trans t: m.transitions){
            result.transitions.add(new Trans(t.state_from + n.states.size()
                + 1, t.state_to + n.states.size() + 1, t.trans_symbol));
        }
        
        result.transitions.add(new Trans(m.states.size() + n.states.size(),
            n.states.size() + m.states.size() + 1, '_'));
       
        // 2 nuevos estados  e intercambiados con m para evitar la repeticion del ultimo elemento de n y el primero de m
        result.final_state = n.states.size() + m.states.size() + 1;
        return result;
    }

   

    
    public static boolean alpha(char c){ return c >= 'a' && c <= 'z';}
    public static boolean alphaM(char c){ return c >= 'A' && c <= 'Z';}
    public static boolean alphaN(char c){ return c >= '0' && c <= '9'; }
    public static boolean alphabet(char c){ return alpha(c) || c == '_' || alphaM(c) || alphaN(c);}
    public static boolean regexOperator(char c){ return c == '(' || c == ')' || c == '*' || c == '|' || c == '.'; }
    public static boolean validAlphabetNoEps(char c){ return alphaN(c) || alphaM(c) || alpha(c); }
    public static boolean validRegExChar(char c){ return alphabet(c) || regexOperator(c); }
    
    public static ArrayList<Character> inputAlphabet;
    
    // validRegEx() - comprueba si la expresión regular es válida
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
       
        
        return l == r; 
    }

    /*
        Dada una expresion regular la convierte a AFND usando el metodo de thompson
    */
    public static AFND compile(String regex)
    {
        if (!validRegEx(regex))
        {
            System.out.println("Expresión regular inválida.");
            return new AFND(); 
        }
        
        if(regex.length() == 1 && alphabet(regex.charAt(0)))
        {
            return new AFND(regex.charAt(0));
        }
        
        Stack <Character> operators = new Stack <Character> ();
        Stack <AFND> operands = new Stack <AFND> ();
        Stack <AFND> concat_stack = new Stack <AFND> ();
        boolean ccflag = false; 
        char op, c; 
        int para_count = 0;
        AFND nfa1, nfa2;

        for (int i = 0; i < regex.length(); i++)
        {
            c = regex.charAt(i);
            if (alphabet(c))
            {
                operands.push(new AFND(c));
                if (ccflag)
                { 
                    operators.push('.');
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
                { 
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
                System.out.println("Error: no hay balance entre operandos y "
                + "operadores");
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
    
  

}