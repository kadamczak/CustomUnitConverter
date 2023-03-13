package com.unitconvertermodel;

import pl.polsl.lab.model.Conversion;
import pl.polsl.lab.model.Unit;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Class responsible for testing the Conversion class.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class ConversionTest {
    
    /**
     * Tested methods:
     * Unit returnOppositeUnit(String)
     * Unit returnOppositeUnit(Unit)
     * 
     * Tested cases:
     * argument exists in the conversion
     * argument is invalid - unit name doesn't exist in the conversion
     * argument is invalid - is null/empty/has whitespace
     */
    
    /**
     * Provides data for the "argument exists in the conversion" test case.
     * 
     * @return test arguments.
     */
     private static Stream<Arguments> testReturnOppositeUnitValid() {
        return Stream.of(
            arguments(new Conversion("a", "b", "+2"), "a", "b"),
            arguments(new Conversion("a", "b", "+2"), "b", "a"),
            arguments(new Conversion("abc", "a", "+2"), "abc", "a"),
            arguments(new Conversion("abc", "a", "+2"), "a", "abc"),
            arguments(new Conversion("m2", "cm2", "+2"), "cm2", "m2")
        );
    }    
    
     /**
      * Tests the "argument exists in the conversion" case.
      * 
      * @param conv conversion
      * @param knownUnit unit name
      * @param expectedName expected other unit.
      */
    @ParameterizedTest
    @MethodSource
    public void testReturnOppositeUnitValid(Conversion conv, String knownUnit, String expectedName){
        String result = conv.returnOppositeUnit(knownUnit);
        String expected = expectedName;
        
        assertEquals(expected, result, 
                     "Method should return the remaining unit.");
    }
    
    /**
     * Provides data for the "argument is invalid - unit name doesn't exist in the conversion" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testReturnOppositeUnitDoesntExist() {
        return Stream.of(
            arguments(new Conversion("a", "b", "+2"), "c"),
            arguments(new Conversion("abc", "a", "+2"), "abz"),
            arguments(new Conversion("a", "b", "+2"), "ab")
        );
    } 
    
    /**
     * Tests the "argument is invalid - unit name doesn't exist in the conversion" case.
     * 
     * @param conv conversion
     * @param knownUnit unit name
     */
    @ParameterizedTest
    @MethodSource
    public void testReturnOppositeUnitDoesntExist(Conversion conv, String knownUnit){
        try{
            String result = conv.returnOppositeUnit(knownUnit);
            fail("Attempt to get an unit opposite to unit that doesn't appear in the conversion should result in an exception.");
        }catch(IllegalArgumentException e){
            
        }
    }
    
    /**
     * Tests the "argument is invalid - is null/empty/has whitespace" case.
     * 
     * @param unit unit name.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "   g  ", "\t"})
    public void testReturnOppositeUnitInvalid(String unit){  
        Conversion conv = new Conversion("a", "b", "+2");
        
        try{
            String result = conv.returnOppositeUnit(unit);
            fail("Attempt to get an unit opposite to a null/empty/containing whitespace unit"
                 + "should result in an exception.");
        }catch(IllegalArgumentException e){      
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tested methods:
     * boolean includesUnit(String)
     * boolean includesUnit(Unit)
     * 
     * Tested cases:
     * argument is valid, unit exists in the conversion
     * argument is valid, unit doesn't exist in the conversion
     * argument is invalid (is null/empty/has whitespace)
     */
    
    /**
     * Provides data for the
     * "argument is valid, unit exists in the conversion" and
     * "argument is valid, unit doesn't exist in the conversion" test cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testIncludesUnitValid() {
        return Stream.of(
            arguments(new Conversion("a", "b", "+2"), "a", true),
            arguments(new Conversion("a", "b", "+2"), "b", true),
            arguments(new Conversion("a", "b", "+2"), "c", false),
            arguments(new Conversion("a", "b", "+2"), "ab", false),
            arguments(new Conversion("a", "b", "+2"), "abc", false),
            arguments(new Conversion("m2", "cm2", "+2"), "cm2", true)
        );
    } 
    
    /**
     * Tests the 
     * "argument is valid, unit exists in the conversion" and
     * "argument is valid, unit doesn't exist in the conversion" cases.
     * 
     * @param conv conversion
     * @param unit unit name
     * @param expected true if unit name is in conversion
     */
    @ParameterizedTest
    @MethodSource
    public void testIncludesUnitValid(Conversion conv, String unit, boolean expected){
        boolean result = conv.includesUnit(unit); 
        assertEquals(expected, result,  "Incorrect search result.");
    }
    
    /**
     * Tests the 
     * "argument is invalid (is null/empty/has whitespace)" case.
     * 
     * @param unit unit name.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "a b", "   g  ", "\t"})
    public void testIncludesUnitInvalid(String unit){
        Conversion conv = new Conversion("a", "b", "+2");
        
        try{
            String result = conv.returnOppositeUnit(unit);
            fail("Attempt to search for an empty or containing whitespace unit should result in an exception.");
        }catch(IllegalArgumentException e){      
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tested methods:
     * boolean includesUnits(String)
     * boolean includesUnits(Unit)
     * 
     * Tested cases:
     * arguments are valid, units exist in the conversion
     * arguments are valid, units don't exist in the conversion
     * arguments are invalid (are null/empty/has whitespace/same value)
     */
    
    
    /**
     * Provides data for the
     * "arguments are valid, units exist in the conversion" and
     * "arguments are valid, units don't exist in the conversion" test cases.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testIncludesUnitsValid() {
        return Stream.of(
            arguments(new Conversion("a", "b", "+2"), "a", "b", true),
            arguments(new Conversion("a", "b", "+2"), "b", "a", true),
            arguments(new Conversion("a", "b", "+2"), "a", "c", false),
            arguments(new Conversion("a", "b", "+2"), "x", "y", false),
            arguments(new Conversion("a", "b", "+2"), "ab", "b", false),
            arguments(new Conversion("ab", "xy", "+2"), "x", "y",  false),
            arguments(new Conversion("m2", "cm2", "+2"), "m2", "cm2", true)
        );
    } 
    
    /**
     * Tests the 
     * "arguments are valid, units exist in the conversion" and
     * "arguments are valid, units don't exist in the conversion" cases.
     * 
     * @param conv conversion
     * @param unit1 first unit name
     * @param unit2 second unit name
     * @param expected true if both units are in the conversion
     */
    @ParameterizedTest
    @MethodSource
    public void testIncludesUnitsValid(Conversion conv, String unit1, String unit2, boolean expected){
        boolean result = conv.includesUnits(unit1, unit2);
        assertEquals(expected, result, "Incorrect search result.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------
        
    /**
     * Provides data for the calculate test.
     * 
     * @return 
     */
    private static Stream<Arguments> testCalculate() {
        return Stream.of(
            arguments
            (new Conversion("a", "b", "+2"), 1, 3),
            arguments
            (new Conversion("a", "b", "-2"), 1, -1),
            arguments
            (new Conversion("a", "b", "/2"), 1, 0.5),
            arguments
            (new Conversion("a", "b", "+2*2"), 1, 6),
            arguments
            (new Conversion("a", "b", "*0.024+1.1/2"), 1.2, 0.5644),
            arguments
            (new Conversion("a", "b", "*100.10"), 0, 0)
                
        );
    }   
    
    /**
     * Tests the calculate method.
     * 
     * @param conv conversion
     * @param value value to be converted
     * @param expected calculation result 
     */
    @ParameterizedTest
    @MethodSource
    public void testCalculate(Conversion conv, double value, double expected){
        double result = conv.calculate(value);
        assertEquals(expected, result, "Conversion returned an incorrect calculated value.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------   
    
    /**
     * Provides data for the toString() test.
     * 
     * @return data arguments.
     */
    private static Stream<Arguments> testToString() {
        return Stream.of(
            arguments
            (new Conversion("a", "b", "+2"), "a b +2"),
            arguments
            (new Conversion("a", "b", "+1-2*3/4"), "a b +1-2*3/4"),
            arguments
            (new Conversion("a", "b", "*0.0000001"), "a b *0.0000001"),
            arguments
            (new Conversion("abc", "xyz", "+2"), "abc xyz +2"),
            arguments
            (new Conversion("m2", "cm2", "*10000"), "m2 cm2 *10000")            
        );
    }   
    
    /**
     * Tests the toString() method.
     * 
     * @param conv Conversion object
     * @param expected expected String.
     */
    @ParameterizedTest
    @MethodSource
    public void testToString(Conversion conv, String expected){
        String result = conv.toString();
        assertEquals(expected, result, "Conversion doesn't return the expected form of toString().");
    }
    
    //--------------------------------------------------------------------------------------------------------------------   

    private static Stream<Arguments> testEqualsValid() {
        return Stream.of(
            arguments(new Conversion("a", "b", "+2"), 
                            new Conversion("a", "b", "+2"), 
                    true),
            arguments(new Conversion("a", "b", "+2"), 
                            new Conversion("a", "c", "+2"), 
                    false),
            arguments(new Conversion("a", "b", "+1"), 
                            new Conversion("a", "b", "+2"), 
                    false)
        );
    }    
    
    /**
     * Tests the equals(Object) method on valid input.
     * 
     * @param c1 first conversion.
     * @param c2 second conversion.
     * @param expected true if contents have the same value.
     */
    @ParameterizedTest
    @MethodSource
    public void testEqualsValid(Conversion c1, Conversion c2, Boolean expected){
        Boolean result = c1.equals(c2);
        assertEquals(expected, result, "Incorrect equals result for different objects.");
    }
    /**
     * Tests the equals(Object) method on same objects.
     */ 
    @Test
    public void testEqualsSame(){
        Conversion c1 = new Conversion("a", "b", "+2");
        Conversion c2 = c1;
        Boolean result = c1.equals(c2);
        assertEquals(true, result, "Incorrect equals result for same objects.");
    }
    /**
     * Tests the equals(Object) method on comparison with null object.
     */ 
    @Test
    public void testEqualsNull(){
        Conversion c1 = new Conversion("a", "b", "+2");
        Conversion c2 = null;
        Boolean result = c1.equals(c2);
        assertEquals(false, result, "Incorrect equals result for null object.");
    }
    
}
