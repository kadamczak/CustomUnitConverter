package pl.polsl.lab.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class representing a value and its associated unit.
 * 
 * @version 2.0
 * @author Kinga Adamczak
 */
public class ValueWithUnit {
    /** Numeric value.*/
    private double value;
    /** Record containing name of unit. It ensures that the format of name is correct. */
    private String unit;
    
    /**
     * Constructor taking in numeric value and name of the unit.
     * 
     * @param v numeric value.
     * @param u unit name.
     */
    public ValueWithUnit(double v, String u){
        this.value = v;
        this.unit = u;
    }
    
//    /**
//     * Constructor taking in numeric value and an Unit record.
//     * 
//     * @param v numeric value.
//     * @param u Unit record.
//     */
//    public ValueWithUnit(double v, Unit u){
//        this.value = v;
//        this.unit = u;
//    }
    
    /**
     * Gets unit name.
     * 
     * @return name of the unit.
     */
    public String getUnit(){
        return unit;
    }
    
    /**
     * Sets unit using a String name.
     * 
     * @param u new name of the unit.
     */
    public void setUnit(String u){
        this.unit = u;
    }
    
//    /**
//     * Sets unit using a record.
//     * 
//     * @param u Unit record.
//     */
//    public void setUnit(Unit u){
//        this.unit = u;
//    }
    
    /**
     * Gets numeric value.
     * 
     * @return numeric floating-point value.
     */
    public double getValue(){
        return value;
    }
    
    /**
     * Sets numeric value.
     * 
     * @param v new numeric floating-point value.
     */
    public void setValue(double v){
        this.value = v;
    }
    
    /**
     * Converts Class instance to String.
     * 
     * @return String consisting of value and unit name separated by space.
     */
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.#");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(5);
        return df.format(value) + " " + unit;
    }

    
}
