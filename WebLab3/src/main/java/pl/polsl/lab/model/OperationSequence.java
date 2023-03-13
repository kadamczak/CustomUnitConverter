package pl.polsl.lab.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing an array of ordered mathematical operations.
 * 
 * @version 2.0
 * @author Kinga Adamczak
 */
public class OperationSequence implements Iterable<Operation>, Serializable{
    /** Array containing singular operations.*/
    private ArrayList<Operation> data;
    
    /**
     * Constructor initializing data array.
     */
    public OperationSequence(){
        data = new ArrayList<>();
    }
    
    /**
     * Constructor initializing operation sequence array with entered data.
     * 
     * @param operations String with mathematical operations typed one after another
     *                   without spaces.
     */
    public OperationSequence(String operations){
        data = new ArrayList<>();    
        loadIndividualOperations(operations);
    }
    
    /**
     * Copy constructor that makes a deep copy of all Operation
     * records from the passed OperationSequence type object.
     * 
     * @param seq OperationSequence record to be copied.
     */
    public OperationSequence(OperationSequence seq){    
        this.data = new ArrayList<>();
        for(Operation o : seq){
            this.data.add(new Operation(o.operator(), o.value()));
        }
    }
    
    /**
     * Loads data array with individual Operation records 
     * created based on input String.
     * 
     * @param operations operation sequence entered as String
     *                   to be divided into individual Operation records.
     */
    private void loadIndividualOperations(String operations){
        OperationFormat format = new OperationFormat();
        Pattern sequenceRegex = format.getOpSequenceRegex();
        Pattern singleOpRegex = Pattern.compile("([\\+\\-\\*\\/]\\d+(\\.\\d+)?)");
        
        Matcher matcher = sequenceRegex.matcher(operations);
        
        matcher = singleOpRegex.matcher(operations);
        while(matcher.find()){
            String individualOperation = matcher.group(0);
            char operator = individualOperation.charAt(0);
            double value = Double.parseDouble(individualOperation.substring(1));       
            
            this.data.add(new Operation(operator, value));
        } 
    }
    
    /**
     * Returns clone of Operation record corresponding to entered index.
     * 
     * @param index entered index.
     * @return Operation clone of record corresponding to entered index.
     */
    public Operation get(int index){
        Operation o = this.data.get(index);
        return new Operation(o.operator(), o.value());
    }
    
    /**
     * Returns amount of operations.
     * 
     * @return amount of operations.
     */
    public int length(){
        return data.size();
    }
    
    /**
     * Adds sequences to the end of data array.
     * 
     * @param opSeq sequence to be added.
     */
    public void add(OperationSequence opSeq){
        if(opSeq == null){
            throw new IllegalArgumentException("Tried to add a null sequence.");
        }
        if(opSeq == this){
            throw new IllegalArgumentException("Tried to add same object.");
        }
        
        for(Operation o : opSeq){
            this.data.add(o);
        }
    }
    
    /**
     * Returns an inverted mathematical operator.
     * 
     * @param operator original operator.
     * @return operator symbol opposite to the entered one.
     */
    private char invertOperator(char operator){
        return switch(operator){
                case '+' -> '-';
                case '-' -> '+';
                case '*' -> '/';
                case '/' -> '*';
                default -> throw new IllegalArgumentException();
            };
    }
    
    /**
     * Fills the data array with reversed order of operations
     * and inverted mathematical operators.
     */
    public void invert(){
        ArrayList<Operation> reversedSequence = new ArrayList<>();
        ListIterator reverseIterator = data.listIterator(data.size());
        
        while(reverseIterator.hasPrevious()) {
            Operation op = (Operation) reverseIterator.previous();         
            char invertedOperator = invertOperator(op.operator());       
            reversedSequence.add(new Operation(invertedOperator, op.value()));
        }
        this.data = reversedSequence;
    }
    
    /**
     * Converts all Operation records to a singular String.
     * 
     * @return String consisting of Operation records unseparated by spaces.
     */
    @Override
    public String toString(){
        String result = "";
        for(Operation o : data){
            result += o.toString();
        }
        return result;
    }
    
    /**
     * Returns an iterator for internal data.
     * 
     * @return iterator for ArrayList of Operation records.
     */
    @Override
    public Iterator<Operation> iterator() {
        return data.iterator();
    }
    
    /**
     * Compares if all operations inside of both objects
     * consist of the same values and are in the same order.
     * 
     * @param o object being compared.
     * @return true if sequences are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (o instanceof OperationSequence) {
            OperationSequence opSeq =  (OperationSequence)o;
            
            if(this.length() != opSeq.length())
                return false;
            
            for(int i = 0; i < data.size(); i++){
                if(!data.get(i).equals(opSeq.get(i)))
                    return false;
            }
            return true;
        }
        return false; 
    }

    /**
     * Returns hash code.
     * 
     * @return hash code assigned to the object.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.data);
        return hash;
    }
    
}
