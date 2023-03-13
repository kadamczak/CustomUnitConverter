package pl.polsl.lab.model;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Class defining a conversion between one unit to another.
 *
 * @version 2.0
 * @author Kinga Adamczak
 */
@Entity
@Table(name = "CONVERSIONS")
@IdClass(ConversionID.class)
public class Conversion implements Serializable {
    /** Unit from which a value is converted.*/
    @Id
    private final String originalUnit;
    /** Resulting unit after conversion.*/
    @Id
    private final String targetUnit;
    
    /** Operation sequence needed to transform a value measured with originalUnit
        to a value measured in targetUnit. */
    @Convert(converter = OperationSequenceConverter.class)
    private OperationSequence operations;
    
    /**
     * Default constructor - generates a random ID for unit symbols
     * and provides a default operation sequence.
     */
    public Conversion(){
        this.originalUnit = UUID.randomUUID().toString();
        this.targetUnit = UUID.randomUUID().toString();
        this.operations = new OperationSequence("*1");
    }
    
     /**
     * Constructor initializing all attributes from Strings.
     * 
     * @param ogUnit original unit name from which a value is converted.
     * @param resultUnit target unit name to which a value is converted.
     * @param opSeq operation sequence written in String form.
     */
    public Conversion(String ogUnit, String resultUnit, String opSeq){  
        this.originalUnit = ogUnit;
        this.targetUnit = resultUnit;
        this.operations = new OperationSequence(opSeq);
    }
    
    /**
     * Constructor initializing all attributes from Strings.
     * 
     * @param ogUnit original unit name from which a value is converted.
     * @param resultUnit target unit name to which a value is converted.
     * @param opSeq operation sequence written in String form.
     */
    public Conversion(String ogUnit, String resultUnit, OperationSequence opSeq){
        this.originalUnit = ogUnit;
        this.targetUnit = resultUnit;
        this.operations = opSeq;
    }
    
    /**
     * Copy constructor making a deep copy of the passed
     * Conversion type object.
     * 
     * @param c Conversion type object to be copied.
     */
    public Conversion(Conversion c){
        this.originalUnit = c.getOriginalUnit();
        this.targetUnit = c.getTargetUnit();
        this.operations = new OperationSequence(c.operations);  
    }
    
    /**
     * Gets name of original unit.
     * 
     * @return name of original unit.
     */
    public String getOriginalUnit(){
        return originalUnit;
    }
    
    /**
     * Gets name of target unit.
     * 
     * @return name of target unit.
     */
    public String getTargetUnit(){
        return targetUnit;
    } 
    
    /**
     * Gets copy of the "operations" attribute.
     * 
     * @return copy of the "operations" attribute.
     */
    public OperationSequence getOperations(){
        OperationSequence seq = new OperationSequence(this.operations);
        return seq;
    }
    
    /**
     * Returns value measured in originalUnit converted
     * to a value measured in targetUnit.
     * 
     * @param originalValue value that needs to be converted from
     *                      being measured in resultUnit to being
     *                      measured in targetUnit.
     * @return numeric floating-point value measured in targetUnit.
     */
    public double calculate(double originalValue){
        double result = originalValue;
        for(Operation o : operations){
            double newValue = o.value();
            switch(o.operator()){
                case '+' -> result += newValue;
                case '-' -> result -= newValue;
                case '*' -> result *= newValue;
                case '/' -> result /= newValue;
                default -> throw new IllegalArgumentException();
            }
        }
        return result;
    }
    
    /**
     * Fills the "operations" attribute with reversed order of operations
     * and inverted mathematical operators.
     */
    public void invertOperationSequence(){
        operations.invert();
    }  
    
    /**
     * Converts Class instance to String.
     * 
     * @return String consisting of original unit, target unit and operation sequence
     *         separated by spaces.
     */
    @Override
    public String toString(){
        return originalUnit + " " + targetUnit + " " + operations.toString();
    }
    
    /**
     * Returns opposite unit to the entered unit record.
     * 
     * @param unit Record known to be in the conversion.
     * @return original unit if the target unit has been entered
     *         or target unit if the original unit has been entered.
     */
    public String returnOppositeUnit(String unit){
        if(originalUnit.equals(unit)) return targetUnit;
        else if (targetUnit.equals(unit)) return originalUnit;
        else
            throw new IllegalArgumentException("Tried to find an unit opposite to an unit that doesn't exist in the conversion.");
    }
    
    /**
     * Checks if entered unit record is included in the conversion.
     * 
     * @param unit record having the same data as the searched unit.
     * @return true if original unit or target unit are equal
     *         to the entered unit.
     */
    public boolean includesUnit(String unit){
        return (originalUnit.equals(unit)|| targetUnit.equals(unit));
    }
    
    /**
     * Checks if both unit records are included in the conversion.
     * 
     * @param unit1 first unit record (doesn't have to be the original unit).
     * @param unit2 second unit record (doesn't have to be the target unit).
     * @return true if both unit records are included in the conversion.
     */
    public boolean includesUnits(String unit1, String unit2){
        if(unit1.equals(unit2))
            throw new IllegalArgumentException("Searched for a conversion between the same unit.");
        
        if(originalUnit.equals(unit1)){
            return targetUnit.equals(unit2);
        } else if (originalUnit.equals(unit2)){
            return targetUnit.equals(unit1);
        } else return false;
    }
    
    /**
     * Checks if compared object has the same unit names and the same
     * values in the operation sequence.
     * 
     * @param o compared object.
     * @return true if objects are the same.
     */
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        
        if (o instanceof Conversion) {
            Conversion conv =  (Conversion)o;
            
            if(!originalUnit.equals(conv.getOriginalUnit()))
                return false;
            if(!targetUnit.equals(conv.getTargetUnit()))
                return false;
            if(!operations.equals(conv.getOperations()))
                return false;
                    
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
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.originalUnit);
        hash = 79 * hash + Objects.hashCode(this.targetUnit);
        hash = 79 * hash + Objects.hashCode(this.operations);
        return hash;
    }
}
