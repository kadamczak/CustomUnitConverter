package pl.polsl.lab.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

//public class Operation implements Serializable{
//    
//    private char operator;
//    private double value;
//    
//    public Operation(){
//        this.operator = '*';
//        this.value = 1.0;
//    }
//    
//    public Operation(char op, double val){
//        this.operator = op;
//        this.value = val;
//    }
//
//    public char getOperator() {
//        return operator;
//    }
//
//    public double getValue() {
//        return value;
//    }
//    
//    
//    
//    /**
//     * Converts record to String.
//     * 
//     * @return String consisting of operator and value unseparated by spaces.
//     */
//    @Override
//    public String toString(){
//        DecimalFormat df = new DecimalFormat("0.#");
//        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
//        df.setMaximumFractionDigits(16);
//        return operator + df.format(value);
//    }  
//}

/**
 * Record storing definition of a singular operation.
 * 
 * @param operator mathematic symbol (+ - * /) of the operation.
 * @param value floating point number taking role of the operand.
 */
public record Operation (char operator, double value){
    /**
     * Converts record to String.
     * 
     * @return String consisting of operator and value unseparated by spaces.
     */
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.#");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(16);
        return operator + df.format(value);
    }  
}