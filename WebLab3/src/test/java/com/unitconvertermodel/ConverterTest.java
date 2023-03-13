package com.unitconvertermodel;

import  pl.polsl.lab.model.Conversion;
import  pl.polsl.lab.model.ConversionContainer;
import  pl.polsl.lab.model.Converter;
import  pl.polsl.lab.model.ValueWithUnit;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.*;

/**
 * Class responsible for testing the Converter class.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class ConverterTest {
    /**Container used by the converter*/
    private ConversionContainer container;
    private Converter converter;
    
    @BeforeEach
    void init(){
        container = new ConversionContainer();
        container.add(new Conversion("°C", "°F", "*9/5+32"));
        container.add(new Conversion("K", "°F", "-273.15*9/5+32"));
        
        container.add(new Conversion("m", "km", "*0.001"));
        container.add(new Conversion("m", "ft", "*3.28084"));
        container.add(new Conversion("ft", "in", "*12"));
        
        converter = new Converter(container);
    }
    
    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tested methods:
     * ValueWithUnit convert(ValueWithUnit, String)
     * ValueWithUnit convert(ValueWithUnit, Unit)
     * 
     * Tested cases:
     * arguments are valid, path between unit exists
     * arguments are valid, path between unit doesn't exist
     * arguments aren't valid
     */
    
    /**
     * Provides data for the
     * "arguments are valid, path between unit exists" test case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testConvertPathDoesExist() {
        return Stream.of(
            arguments(new ValueWithUnit(10.5, "°C"), "K", 283.65),
            arguments(new ValueWithUnit(10.5, "°F"), "°C", -11.94444),
            arguments(new ValueWithUnit(10.5, "km"), "in", 413385.8),
            arguments(new ValueWithUnit(10.5, "ft"), "km", 0.0032),
            arguments(new ValueWithUnit(10.5, "km"), "m", 10500)
        );
    }  
    
    /**
     * Tests the
     * "arguments are valid, path between unit exists" case.
     * 
     * @param input input value with unit.
     * @param targetUnit name of resulting unit
     * @param expected expected value and associated unit
     */
    @ParameterizedTest
    @MethodSource
    public void testConvertPathDoesExist(ValueWithUnit input, String targetUnit, double expected){
        ValueWithUnit result = converter.convert(input, targetUnit);
        
        assertEquals(expected, result.getValue(), 0.1, "Incorrect conversion result.");
    }
    
    /**
     * Provides data for the 
     * "arguments are valid, path between unit doesn't exist" test case.
     * 
     * @return test arguments
     */
    private static Stream<Arguments> testConvertPathDoesNotExist() {
        return Stream.of(
            arguments(new ValueWithUnit(10.5, "A"), "B"),
            arguments(new ValueWithUnit(10.5, "m"), "°C"),
            arguments(new ValueWithUnit(10.5, "K"), "°in"),
            arguments(new ValueWithUnit(10.5, "m"), "B")
        );
    }  
    
    /**
     * Tests the 
     * "arguments are valid, path between unit doesn't exist" case.
     * 
     * @param input original value and unit
     * @param targetUnit resulting unit
     */
    @ParameterizedTest
    @MethodSource
    public void testConvertPathDoesNotExist(ValueWithUnit input, String targetUnit){
        ValueWithUnit result = converter.convert(input, targetUnit);
        
        assertEquals(null, result, "Returned result despite conversion not existing.");
    }
    
}
