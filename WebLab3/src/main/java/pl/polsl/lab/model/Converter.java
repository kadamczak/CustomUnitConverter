package pl.polsl.lab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class converting values to different units
 * using a reference to an existing ConversionContainer
 * type object as a conversion database.
 * 
 * @version 2.0
 * @author Kinga Adamczak
 */
public class Converter {
    /** Reference to conversions known by the class.*/
    private ConversionContainer data;
    
    /**
     * Constructor setting a reference to a ConversionContainer
     * type object.
     * 
     * @param conv reference to ConversionContainer type object.
     */
    public Converter(ConversionContainer conv){
        this.data = conv;
    }
    
    /**
     * Returns reference to the container that is the source of
     * converter's data.
     * 
     * @return data that the converter is operating on.
     */
    public ConversionContainer getData(){
        return data;
    }
    
    /**
     * Returns operation sequence needed to convert value measured
     * with startUnit to a value measured with endUnit.
     * 
     * @param startUnit original unit.
     * @param endUnit target unit.
     * @return requested operation sequence, null if direct conversion between startUnit
     *         and endUnit doesn't exist.
     */
    private OperationSequence findOperationSequence(String startUnit, String endUnit){
        Conversion conv = data.returnConversion(startUnit, endUnit);
        if(conv == null) return null;
        String target = conv.getTargetUnit();
        
        if(target.equals(startUnit))
            conv.invertOperationSequence();
        return conv.getOperations();
    }
    
     /**
     * Creates a path from startUnit to endUnit using data
     * from the passed HashMap.
     * 
     * @param startUnit original unit.
     * @param endUnit target unit.
     * @param mapToPrevious maps units to the previous unit in their path
     *                      from the startUnit.
     * @return ArrayList containing ordered units whose conversions
     *         ultimately lead to the endUnit.
     */
    private ArrayList<String> createPath(String startUnit, String endUnit,
                                         HashMap<String, String> mapToPrevious){
        ArrayList<String> path = new ArrayList<>();
        path.add(endUnit);
        
        String parentUnit = "";
        String childUnit = endUnit;
        while(!parentUnit.equals(startUnit)){
            parentUnit = mapToPrevious.get(childUnit);
            path.add(parentUnit);
            childUnit = parentUnit;
        }
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Finds path from startUnit to endUnit using a breadth-first search.
     * Gradually fills out a HashMap mapping reachable units to a previous unit
     * in path.
     * 
     * @param startUnit original unit.
     * @param endUnit target unit.
     * @return ArrayList containing ordered units whose conversions
     *         ultimately lead to the endUnit. Null if such a path doesn't exist.
     */
    private ArrayList<String> findUnitPath(String startUnit, String endUnit){
        ArrayList<String> excluded = new ArrayList<>();
        ArrayList<String> queue = new ArrayList<>();
        
        excluded.add(startUnit);
        queue.add(startUnit);
        
        //child -> parent
        HashMap<String, String> mapToPrevious = new HashMap<>();
        
        while(!queue.isEmpty()){
            String currentUnit = queue.remove(0);
            ArrayList<String> currentResult = data.findAllDirectConversions(currentUnit, excluded);
            excluded.addAll(currentResult);
            queue.addAll(currentResult);
            for(String childUnit : currentResult){
                mapToPrevious.put(childUnit, currentUnit);
            }
            if(currentResult.contains(endUnit))
                return createPath(startUnit, endUnit, mapToPrevious);
        }
             
        return null;
    }
    
    /**
     * Creates a Conversion type object needed to convert value
     * measured with startUnit to a value measured with endUnit.
     * The units don't have to be directly connected to each other - method will
     * find the shortest path between them and create the operation sequence
     * inside Conversion type object accordingly.
     * 
     * @param startUnit original unit.
     * @param endUnit target unit.
     * @return Conversion type object with an operation sequence connecting
     *         the two units or null if no path between them is found.
     */
    private Conversion createConversion(String startUnit, String endUnit){
        ArrayList<String> unitPath = findUnitPath(startUnit, endUnit);
        if(unitPath == null)
            return null;
        
        OperationSequence fullSequence = new OperationSequence();
        for(int i = 0; i < unitPath.size() - 1; i++){
            OperationSequence seq = findOperationSequence(unitPath.get(i), 
                                                          unitPath.get(i+1));
            fullSequence.add(seq);
        }
        
        return new Conversion(startUnit, endUnit, fullSequence);
    }
    
//    /**
//     * Converts a value measured in one unit to a value
//     * measured in another unit. Units don't need to have a direct
//     * connection between them.
//     * 
//     * @param input value with the original unit.
//     * @param targetUnitName name of the new unit that the value should be measured with.
//     * @return input value converted to be measured with targetUnit, null
//     *         if there is no connection between the two units.
//     */
//    public ValueWithUnit convert(ValueWithUnit input, String targetUnitName){
//        Unit targetUnit = new Unit(targetUnitName);
//        return convert(input, targetUnit);
//    }
    
    /**
     * Converts a value measured in one unit to a value
     * measured in another unit. Units don't need to have a direct
     * connection between them.
     * 
     * @param input value with the original unit.
     * @param targetUnit new unit that the value should be measured with.
     * @return input value converted to be measured with targetUnit, null
     *         if there is no connection between the two units.
     */
    public ValueWithUnit convert(ValueWithUnit input, String targetUnit){
        double originalValue = input.getValue();
        String originalUnit = input.getUnit();
        
        Conversion conversion = createConversion(originalUnit, targetUnit);
        if(conversion == null)
            return null;

        double calculatedValue = conversion.calculate(originalValue);
        return new ValueWithUnit(calculatedValue, targetUnit);       
    }
}
