package com.unitconvertermodel;

import pl.polsl.lab.model.Conversion;
import pl.polsl.lab.model.ConversionContainer;
import pl.polsl.lab.model.MetricPrefixesMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.*;

/**
 * Class responsible for testing the ConversionContainer class.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class ConversionContainerTest {
    
    /**Container that has elements.*/
    ConversionContainer containerFull;
    /**Container that does not have elements.*/
    ConversionContainer containerEmpty;
    /**Container that has specific elements useful for testing metric conversion generation.*/
    ConversionContainer containerMetric;
    
    @BeforeEach
    public void init(){
        containerFull = new ConversionContainer();
        containerFull.add(new Conversion("a", "b", "+2"));
        containerFull.add(new Conversion("c", "d", "+2"));
        containerFull.add(new Conversion("ab", "xy", "+1*2"));
        containerFull.add(new Conversion("c", "a", "+2"));
        
        containerEmpty = new ConversionContainer();
        
        containerMetric = new ConversionContainer();
        containerMetric.add(new Conversion("m", "km", "/1000"));
        containerMetric.add(new Conversion("cm", "m", "/100"));
        containerMetric.add(new Conversion("m", "ft", "*3.28084"));
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tests for methods:
     * Conversion returnConversion(String, String)
     * Conversion returnConversion(Unit, Unit)
     * 
     * Tested cases:
     * container has elements, arguments are valid and a conversion has been found - returns Conversion
     * container has elements, arguments are valid and a conversion hasn't been found - returns null
     * container does not have elements - returns null
     * arguments aren't valid (are null/empty/contain whitespace/are the same values) - throws IllegalArgumentException 
     */
    
    /**
     * Provides data for the "container has elements, arguments are valid and a conversion has been found - returns Conversion"
     * and "container has elements, arguments are valid and a conversion hasn't been found - returns null" cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testReturnConversionValid() {
        return Stream.of(
            arguments("a", "b", new Conversion("a", "b", "+2")),
            arguments("b", "a", new Conversion("a", "b", "+2")),
            arguments("ab", "xy", new Conversion ("ab", "xy", "+1*2")),
            arguments("a", "d", null),
            arguments("y", "z", null)
        );
    }  
    
    /**
     * Tests the "container has elements, arguments are valid and a conversion has been found - returns Conversion"
     * and "container has elements, arguments are valid and a conversion hasn't been found - returns null" cases.
     * 
     * @param unit1 first unit name.
     * @param unit2 second unit name.
     * @param expected found Conversion object or null if nothing was found.
     */
    @ParameterizedTest
    @MethodSource 
    public void testReturnConversionValid(String unit1, String unit2, Conversion expected){
        Conversion result = containerFull.returnConversion(unit1, unit2);
        assertEquals(expected,result,  "Incorrect result of search.");
    }
    
    /**
     * Provides data for the "container does not have elements - returns null" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testReturnConversionEmptyContainer() {
        return Stream.of(
            arguments("a", "b"),
            arguments("b", "a"),
            arguments("ab", "xy"),
            arguments("a", "c"),
            arguments("m2", "cm2")
        );
    }  
    
    /**
     * Tests the "container does not have elements - returns null" case.
     * 
     * @param unit1 first unit name.
     * @param unit2 second unit name.
     */
    @ParameterizedTest
    @MethodSource 
    public void testReturnConversionEmptyContainer(String unit1, String unit2){      
        Conversion result = containerEmpty.returnConversion(unit1, unit2);
        assertEquals(null,result, "Returned a conversion despite container being empty.");
    }
   
    
    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tests for methods:
     * int findIndexOf(String, String)
     * int findIndexOf(Unit, Unit)
     * 
     * Tested cases:
     * container has elements, arguments are valid and a conversion has been found - returns int (index in the container)
     * container has elements, arguments are valid and a conversion hasn't been found - returns int (-1)
     * container does not have elements - returns int (-1)
     * arguments aren't valid (are null/empty/contain whitespace/are the same values) - throws IllegalArgumentException 
     */
    
    
    /**
     * Provides data for the
     * "container has elements, arguments are valid and a conversion has been found - returns int (index in the container)"
     * and "container has elements, arguments are valid and a conversion hasn't been found - returns int (-1)" cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testFindIndexOfValid() {
        return Stream.of(
            arguments("a", "b", 0),
            arguments("b", "a", 0),
            arguments("ab", "xy", 2),
            arguments("a", "d", -1),
            arguments("y", "z", -1)
        );
    } 
    
    /**
     * Tests the
     * "container has elements, arguments are valid and a conversion has been found - returns int (index in the container)"
     * and "container has elements, arguments are valid and a conversion hasn't been found - returns int (-1)" cases.
     * 
     * @param unit1 first unit name.
     * @param unit2 second unit name.
     * @param expected index of found conversion or -1 if nothing was found.
     */
    @ParameterizedTest
    @MethodSource
    public void testFindIndexOfValid(String unit1, String unit2, int expected){
        int result = containerFull.findIndexOf(unit1, unit2);
        
        assertEquals(expected,result,  "Returned wrong index of searched conversion.");
    }
    
    /**
     * Provides data for the "container does not have elements - returns int (-1)" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testFindIndexOfEmptyContainer() {
        return Stream.of(
            arguments("a", "b"),
            arguments("b", "a"),
            arguments("ab", "xy"),
            arguments("a", "c"),
            arguments("m2", "cm2")
        );
    }  
    
    /**
     * Tests the "container does not have elements - returns int (-1)" case.
     * 
     * @param unit1 first unit name.
     * @param unit2 second unit name.
     */
    @ParameterizedTest
    @MethodSource 
    public void testFindIndexOfEmptyContainer(String unit1, String unit2){      
        int result = containerEmpty.findIndexOf(unit1, unit2);
        assertEquals(-1,result,  "Returned index above -1 despite container being empty.");
    }

    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tests for method:
     * boolean add(Conversion)
     * 
     * Tested cases:
     * container has elements, conversion between units in the Conversion object doesn't already exist - returns true
     * container has elements, conversion between units in the Conversion object already exists - returns false
     * container doesn't have elements - returns true
     */
    
    /**
     * Provides data for the
     * "container has elements, conversion between units in the Conversion object doesn't already exist - returns true"
     * and "container has elements, conversion between units in the Conversion object already exists - returns false" cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testAddFull() {
        return Stream.of(
            arguments(new Conversion("x", "z", "+2"), true),
            arguments(new Conversion("abc", "xyz", "+2"), true),
            arguments(new Conversion("A", "B", "+2"), true),
            arguments(new Conversion("a", "b", "+2"), false),
            arguments(new Conversion("a", "b", "*6"), false),
            arguments(new Conversion("m2", "cm2", "*10.0+2"), true)
        );
    }  
    
    /**
     * Tests the
     * "container has elements, conversion between units in the Conversion object doesn't already exist - returns true"
     * and "container has elements, conversion between units in the Conversion object already exists - returns false" cases.
     * 
     * @param newConversion Conversion to be added.
     * @param expected true if object should be added to container.
     */
    @ParameterizedTest
    @MethodSource
    public void testAddFull(Conversion newConversion, boolean expected){
        boolean result = containerFull.add(newConversion);
        
        assertEquals(expected,result,  "Wrong outcome of add operation.");
    }
    
    /**
     * Provides data for the
     * "container doesn't have elements - returns true" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testAddEmpty() {
        return Stream.of(
            arguments(new Conversion("x", "z", "+2")),
            arguments(new Conversion("abc", "xyz", "+2")),
            arguments(new Conversion("A", "B", "+2")),
            arguments(new Conversion("a", "b", "+2")),
            arguments(new Conversion("a", "b", "*6")),
            arguments(new Conversion("m2", "cm2", "*10.0+2"))
        );
    }  
    
    /**
     * Tests the
     * "container doesn't have elements - returns true" case.
     * 
     * @param newConversion Conversion to be added.
     */
    @ParameterizedTest
    @MethodSource
    public void testAddEmpty(Conversion newConversion){
        boolean result = containerEmpty.add(newConversion);
        
        assertEquals(true,result, "Wrong outcome of add operation.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tests for methods:
     * boolean delete(String, String)
     * boolean delete(Unit, Unit)
     * 
     * Tested cases:
     * container has elements, conversion between units exists - returns true
     * container has elements, conversion between units doesn't exist - returns false
     * container doesn't have elements - returns false
     * arguments aren't valid (are null/empty/contain whitespace/are the same values) - throws IllegalArgumentException 
     */
    
    /**
     * Provides data for the
     * "container has elements, conversion between units exists - returns true" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testDeleteFull() {
        return Stream.of(
            arguments("a", "b", true),
            arguments("c", "d", true),
            arguments("ab", "xy", true),
            arguments("a", "d", false),
            arguments("a", "x", false),
            arguments("i", "j", false)
        );
    }  
    
    /**
     * Tests the
     * "container has elements, conversion between units exists - returns true" and
     * "container has elements, conversion between units doesn't exist - returns false" cases.
     * 
     * @param unit1 first unit name.
     * @param unit2 first unit name.
     * @param expected true if an object should be deleted.
     */
    @ParameterizedTest
    @MethodSource
    public void testDeleteFull(String unit1, String unit2, boolean expected){
        boolean result = containerFull.delete(unit1, unit2);
        
        assertEquals(expected, result,"Wrong outcome of delete operation.");
    }
    
    /**
     * Provides data for the
     * "container doesn't have elements - returns false" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testDeleteEmpty() {
        return Stream.of(
            arguments("a", "b"),
            arguments("ab", "xy"),
            arguments("a", "d")
        );
    }  
    
    /**
     * Tests the
     * "container doesn't have elements - returns false" case.
     * 
     * @param unit1 first name of unit.
     * @param unit2 second name of unit.
     */
    @ParameterizedTest
    @MethodSource
    public void testDeleteEmpty(String unit1, String unit2){
        boolean result = containerEmpty.delete(unit1, unit2);
        
        assertEquals(false,result,
                     "Deletion attempt inside an empty container should always return false.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tested methods:
     * void addMetricConversions(String)
     * void addMetricConversions(Unit)
     * 
     * Tested cases:
     * argument is valid 
     * argument is invalid (is null/empty/contains whitespace/has a prefix)
     * 
     */
    
    /**
     * Fills the expected container.
     * 
     * @param expected model container created after the "argument is valid" test.
     */
    void fillMetricExpected(ConversionContainer expected){
        expected.add(new Conversion("m", "km", "/1000"));
        expected.add(new Conversion("cm", "m", "/100"));
        expected.add(new Conversion("m", "ft", "*3.28084"));
        
        MetricPrefixesMap map = new MetricPrefixesMap();
        
        for(String key : map.keySet()){
            String prefixedUnit = key + "m";
            if(!prefixedUnit.equals("km") && !prefixedUnit.equals("cm"))
                expected.add(new Conversion("m", prefixedUnit, map.returnMultiplier(key)));
        }
    }
    
    /**
     * Tests the "argument is valid" case.
     */
    @Test
    public void addMetricConversionsValid(){
        ConversionContainer expected = new ConversionContainer();
        fillMetricExpected(expected);
        
        containerMetric.addMetricConversions("m");
        assertEquals(expected, containerMetric, "Metric values added incorrectly.");
    }
    
    
    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tested methods:
     * ArrayList<String> findAllDirectConversions(String, ArrayList<String>)
     * ArrayList<String> findAllDirectConversions(Unit, ArrayList<String>)
     * 
     * Tested cases:
     * arguments are valid, unit has direct conversions
     * arguments are valid, unit doesn't have direct conversions
     * arguments are valid, unit has direct conversions but they are excluded
     * arguments are invalid (unit name is null/empty/contains whitespace)
     * 
     */
    
    /**
     * Provides data for the
     * "arguments are valid, unit has direct conversions",
     * "arguments are valid, unit doesn't have direct conversions"
     * and "arguments are valid, unit has direct conversions but they are excluded" test cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testFindAllDirectConversionsValid() {
        return Stream.of(
            arguments
            ("a",
             new ArrayList<String> (),
             new ArrayList<String>(Arrays.asList("b", "c"))
            ),
            arguments(
            "c",
            new ArrayList<String>(Arrays.asList("d")),
            new ArrayList<String>(Arrays.asList("a"))
            ),
            arguments(
            "xyz",
            new ArrayList<String>(),
            new ArrayList<String>()
            )
        );
    }
    
    /**
     * Tests the 
     * "arguments are valid, unit has direct conversions",
     * "arguments are valid, unit doesn't have direct conversions"
     * and "arguments are valid, unit has direct conversions but they are excluded" cases.
     * 
     * @param unit unit name.
     * @param excluded unit names that should be ignored.
     * @param expected model result.
     */
    @ParameterizedTest
    @MethodSource
    public void testFindAllDirectConversionsValid(String unit, ArrayList<String> excluded, ArrayList<String> expected){
        ArrayList<String> result = containerFull.findAllDirectConversions(unit, excluded);
        
        assertEquals(expected,result, "Incorrect direct search result.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tests if container that has data is fully cleared after using clear() method.
     */
    @Test
    public void testClearFull(){
        containerFull.clear();
        ConversionContainer expected = new ConversionContainer();
        assertEquals(expected, containerFull, "Container wasn't cleared.");
    }
    
    /**
     * Tests if container that doesn't have data is fully cleared after using clear() method.
     */
    @Test
    public void testClearEmpty(){
        containerEmpty.clear();
        ConversionContainer expected = new ConversionContainer();
        assertEquals(expected, containerEmpty, "Container wasn't cleared.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    private static Stream<Arguments> testExistsValid() {
        return Stream.of(
            arguments("a", "b", true),
            arguments("b", "a", true),
            arguments("c", "d", true),
            arguments("c", "b", false),
            arguments("x", "yy", false)
        );
    } 
    /**
     * Tests if container correctly tells if it contains specified conversions when input data is valid.
     * 
     * @param unit1 name of first unit.
     * @param unit2 name of second unit.
     * @param expected true if conversion between units exists.
     */
    @ParameterizedTest
    @MethodSource
    public void testExistsValid(String unit1, String unit2, Boolean expected){
        Boolean result = containerFull.exists(unit1, unit2);
        assertEquals(expected, result, "Incorrect find resulsts.");
    }  
    
    //--------------------------------------------------------------------------------------------------------------------
    
}
