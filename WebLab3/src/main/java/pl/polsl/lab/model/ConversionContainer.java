package pl.polsl.lab.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class storing Conversion class instances in a form of an ArrayList.
 * 
 * @version 2.0
 * @author Kinga Adamczak
 */
public class ConversionContainer implements Iterable<Conversion>{
    /** ArrayList of conversions defined in the program.*/
    private final ArrayList<Conversion> data;
    
    /**
     * Constructor initializing data ArrayList.
     */
    public ConversionContainer(){
        data = new ArrayList<>();
    }
    
    /**
     * Returns length of data container.
     * 
     * @return length of data container.
     */
    public int length(){
        return data.size();
    }
    
    /**
     * Returns clone of Conversion type object corresponding to entered index.
     * 
     * @param index entered index.
     * @return clone of Conversion type object corresponding to entered index.
     */
    public Conversion get(int index){
        return new Conversion(data.get(index));
    }  
    
    /**
     * Returns reference to container's data.
     * 
     * @return reference to container's data.
     */
    public ArrayList<Conversion> getData(){
        return data;
    }
    
//    /**
//     * Gets a deep copy of a conversion between specified
//     * unit names. Order of units doesn't matter.
//     * 
//     * @param unitName1 first unit name.
//     * @param unitName2 second unit name.
//     * @return copy of a Conversion type object containing unit1 and unit2.
//     *         Returns null if no such object is found.
//     */
//    public Conversion returnConversion(String unitName1, String unitName2){
//        Unit unit1 = new Unit(unitName1);
//        Unit unit2 = new Unit(unitName2);
//        
//        return returnConversion(unit1, unit2);
//    }
    
    /**
     * Gets a deep copy of a conversion between specified
     * unit records. Order of units doesn't matter.
     * 
     * @param unit1 first unit record.
     * @param unit2 second unit record.
     * @return copy of a Conversion type object containing unit1 and unit2.
     *         Returns null if no such object is found.
     */
    public Conversion returnConversion(String unit1, String unit2){
        if(unit1.equals(unit2))
            throw new IllegalArgumentException("Searched for a conversion between the same unit.");
        
        for(Conversion c : data){
            if(c.includesUnits(unit1, unit2)) return new Conversion(c);
        }
        return null;
    }
    
//    /**
//     * Returns index of Conversion type object defining
//     * conversion between passed unit names (order doesn't matter).
//     * 
//     * @param unitName1 first name of unit.
//     * @param unitName2 second name of unit.
//     * @return index if object was found, -1 if not.
//     */
//    public int findIndexOf(String unitName1, String unitName2){
//        Unit unit1 = new Unit(unitName1);
//        Unit unit2 = new Unit(unitName2);
//        
//        return findIndexOf(unit1, unit2);
//    }
    
    /**
     * Returns index of Conversion type object defining
     * conversion between passed unit records (order doesn't matter).
     * 
     * @param unit1 first unit record.
     * @param unit2 second unit record.
     * @return index if object was found, -1 if not.
     */
    public int findIndexOf(String unit1, String unit2){
        if(unit1.equals(unit2))
            throw new IllegalArgumentException("Searched for a conversion between the same unit.");
        
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).includesUnits(unit1, unit2))
                return i;
        }
        return -1;
    }
//    
//    /**
//     * Returns true or false depending if conversion between specified units exists
//     * in the database (order of units doesn't matter).
//     * 
//     * @param unitName1 name of first unit.
//     * @param unitName2 name of second unit.
//     * @return true if conversion exists.
//     */
//    public Boolean exists(String unitName1, String unitName2){
//        Unit unit1 = new Unit(unitName1);
//        Unit unit2 = new Unit(unitName2);
//        return exists(unit1, unit2);
//    }
    
    /**
     * Returns true or false depending if conversion between specified units exists
     * in the database (order of units doesn't matter).
     * 
     * @param unit1 first unit's object.
     * @param unit2 second unit's object.
     * @return true if conversion exists.
     */
    public Boolean exists(String unit1, String unit2){
        if(findIndexOf(unit1, unit2) == -1)
            return false;
        return true;
    }
    
    /**
     * Adds a Conversion type object to data if conversion
     * between its units doesn't already exist.
     * 
     * @param newConversion Conversion type object to be added to data.
     * @return true if a new Conversion type object was added
     *         (which means it wasn't a duplicate of a conversion
     *          between the same two units).
     */
    public boolean add(Conversion newConversion){
        String unit1 = newConversion.getOriginalUnit();
        String unit2 = newConversion.getTargetUnit();

        if(findIndexOf(unit1, unit2) == -1)
            return data.add(newConversion);
        else
            return false;
    }
    
//    /**
//     * Deletes a conversion between passed unit names.
//     * Order of units doesn't matter.
//     * 
//     * @param unitName1 first name of unit.
//     * @param unitName2 second name of unit.
//     * @return true if an object was actually deleted
//     *         (which means that a conversion between
//     *          unit1 and unit2 existed).
//     */
//    public boolean delete(String unitName1, String unitName2){
//        Unit unit1 = new Unit(unitName1);
//        Unit unit2 = new Unit(unitName2);
//        
//        return delete(unit1, unit2);
//    }
    
    /**
     * Attempts to delete a conversion from data and returns true if
     * element was found and deleted.
     * 
     * @param conv object to be deleted.
     * @return true if object existed in the array and got deleted.
     */
    public boolean delete(Conversion conv){
        return data.remove(conv);
    }
    
    /**
     * Clears all data from container.
     */
    public void clear(){
        this.data.clear();
    }
    
    /**
     * Deletes a conversion between passed units records.
     * Order of units doesn't matter.
     * 
     * @param unit1 first unit record.
     * @param unit2 second unit record.
     * @return true if an object was actually deleted
     *         (which means that a conversion between
     *          unit1 and unit2 existed).
     */
    public boolean delete(String unit1, String unit2){
        if(unit1.equals(unit2))
            throw new IllegalArgumentException("Searched for a conversion between the same unit.");
        
        int index = this.findIndexOf(unit1, unit2);
        if(index == -1) return false;
        data.remove(index);
        return true;
    }
    
    /**
     * Checks if the unit's name does not begin with a metric prefix.
     * 
     * @param unit unit record.
     * @return true if unit doesn't begin with any of the metric prefixes
     *         (units that consist only of letters of a prefix, for example "m", are allowed).
     */
    private boolean checkIfNoPrefix(Unit unit){
        NoPrefixUnitFormat format = new NoPrefixUnitFormat();
        Pattern regex = format.getNoPrefixRegex();
        Matcher matcher = regex.matcher(unit.getSymbol());
        return matcher.matches();
    }
    
    /**
     * Creates all missing conversions between a base metric unit passed as string
     * and its prefixed versions.
     * 
     * @param baseUnitName base metric unit name.
     */
    public void addMetricConversions(String baseUnitName){
        Unit baseUnit = new Unit(baseUnitName);
        addMetricConversions(baseUnit);
    }
    
    /**
     * Creates all missing conversions between a base metric unit record
     * and its prefixed versions.
     * 
     * @param baseUnit base metric unit record.
     */
    public void addMetricConversions(Unit baseUnit){
        if(!checkIfNoPrefix(baseUnit))
            throw new IllegalArgumentException("Tried to create metric conversions for an already prefixed unit.");
        
        MetricPrefixesMap metricPrefixes = new MetricPrefixesMap(); 
        
        for(String prefix : metricPrefixes.keySet()){
            String prefixedUnit = prefix + baseUnit.getSymbol();
            String operation = metricPrefixes.returnMultiplier(prefix);
            Conversion c = new Conversion(baseUnit.getSymbol(), prefixedUnit, operation);
            add(c);
        }
    }
    
//    /**
//     * Finds all Conversion type objects in the data array
//     * that include a specified unit name.
//     * 
//     * @param unitName name of searched for unit.
//     * @param excluded array of units that won't be added to the
//     *                 returned array even if they have a direct conversion
//     *                 with passed unit.
//     * @return ArrayList of found unit names.
//     */
//    public ArrayList<String> findAllDirectConversions(String unitName, ArrayList<String> excluded){
//        Unit unit = new Unit(unitName);
//        return findAllDirectConversions(unit, excluded);
//    }
    
    /**
     * Finds all Conversion type objects in the data array
     * that include a specified unit record.
     * 
     * @param unit searched for unit record.
     * @param excluded array of units that won't be added to the
     *                 returned array even if they have a direct conversion
     *                 with passed unit.
     * @return ArrayList of found unit names.
     */
    public ArrayList<String> findAllDirectConversions(String unit, ArrayList<String> excluded){   
        return data.stream()
               .filter(c -> c.includesUnit(unit))
               .filter(c -> !excluded.contains(c.returnOppositeUnit(unit)))
               .map(c -> c.returnOppositeUnit(unit))
               .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Returns an iterator for internal data.
     * 
     * @return iterator for ArrayList of Conversion type objects.
     */
    @Override
    public Iterator<Conversion> iterator() {
        return data.iterator();
    }
    
    /**
     * Compares if both containers include the same conversions (and only them).
     * 
     * @param o compared container
     * @return true if containers have the same data.
     */
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        
        if (o instanceof ConversionContainer) {
            ConversionContainer conv =  (ConversionContainer)o;
            
            if(this.length() != conv.length())
                return false;
            
            for(int i = 0; i < data.size(); i++){
                if(!data.get(i).equals(conv.get(i)))
                    return false;
            }
            return true;
        }
        return false; 
    }
}
