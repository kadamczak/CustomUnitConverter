package com.unitconvertermodel;

import pl.polsl.lab.model.NoPrefixUnitFormat;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Class responsible for testing the NoPrefixUnitFormat class.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class NoPrefixUnitFormatTest {

    NoPrefixUnitFormat format;
    
    @BeforeEach
    public void init(){
        format = new NoPrefixUnitFormat();
    }
    
    private static Stream<Arguments> testCheckCorrectnessValid() {
        return Stream.of(
            arguments("g", true),
            arguments("m", true),
            arguments("kg", false),
            arguments("dag", false),
            arguments("a b", false),
            arguments("", false),
            arguments(" ", false)
        );
    }  
    
    /**
     * Tests if non-null strings match regex.
     * 
     * @param input entered text.
     * @param expected true if text should match.
     */
    @ParameterizedTest
    @MethodSource
    public void testCheckCorrectnessValid(String input, Boolean expected){
        Boolean result = format.checkCorrectness(input);
        assertEquals(expected, result, "Incorrect regex check.");
    }
    /**
     * Tests case when null is passed into the checkCorrectness(String) method.
     */
    @Test
    public void testCheckCorrectnessNull(){
        try{
            Boolean result = format.checkCorrectness(null);
            fail("Entering null as text should result with an exception.");
        }catch(IllegalArgumentException e){     
        }
    }
    
}
