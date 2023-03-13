package com.unitconvertermodel;

import pl.polsl.lab.model.Operation;
import pl.polsl.lab.model.OperationSequence;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Class responsible for testing OperationSequence class.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class OperationSequenceTest {
    
    /**
     * Tested methods:
     * void add(OperationSequence)
     * 
     * Tested cases:
     * both sequences have elements
     * at least one of sequences doesn't have elements
     * added sequence is null
     * sequences are the same objects
     * 
     */
    
    /**
     * Provides data for the
     * "both sequences have elements" test case.
     * 
     * @return test arguments
     */
    private static Stream<Arguments> testAddBothFull() {
        return Stream.of(
            arguments("+1", "+2", "+1+2"),
            arguments("+1+2", "+3", "+1+2+3"),
            arguments("+1", "+2+3", "+1+2+3"),
            arguments("+2*3", "+2*3", "+2*3+2*3"),
            arguments("+2*3/4.02", "-1", "+2*3/4.02-1"),
            arguments("*0.001+2+3/4.01", "+0.1+2+40.1", "*0.001+2+3/4.01+0.1+2+40.1")
        );
    }    
 
    /**
     * Tests the
     * "both sequences have elements" case.
     * 
     * @param opArgs1 contents of first sequence
     * @param opArgs2 contents of second sequence
     * @param expected contents of resulting sequence
     */
    @ParameterizedTest
    @MethodSource
    public void testAddBothFull(String opArgs1, String opArgs2, String expected){
        OperationSequence opSeq1 = new OperationSequence(opArgs1);
        OperationSequence opSeq2 = new OperationSequence(opArgs2);    
        OperationSequence opSeqExpected = new OperationSequence(expected);
        
        opSeq1.add(opSeq2);
        
        assertEquals(opSeqExpected,opSeq1,
        "Resulting sequence should consist of the original sequence plus the added sequence at the end.");
    }
    
    /**
     * Provides data for the
     * "at least one of sequences doesn't have elements" test case.
     * 
     * @return test arguments
     */
    private static Stream<Arguments> testAddEmpty() {
        return Stream.of(
            arguments
            (new OperationSequence(), new OperationSequence(), new OperationSequence()),
            arguments
            (new OperationSequence(), new OperationSequence("+2-3"), new OperationSequence("+2-3")),
            arguments
            (new OperationSequence("+2-3"), new OperationSequence(), new OperationSequence("+2-3"))
        );
    }    
    
    /**
     * Tests the
     * "at least one of sequences doesn't have elements" case.
     * 
     * @param seq1 contents of first sequence
     * @param seq2 contents of second sequence
     * @param expected contents of resulting sequence
     */
    @ParameterizedTest
    @MethodSource
    public void testAddEmpty(OperationSequence seq1, OperationSequence seq2, OperationSequence expected){
        seq1.add(seq2);
        
        assertEquals(expected, seq1,
        "Resulting sequence should consist of the original sequence plus the added sequence at the end.");
    }
    
    /**
     * Tests the "added sequence is null" case.
     */
    @Test
    public void testAddNull(){
        OperationSequence seq1 = new OperationSequence("+2");
        OperationSequence seq2 = null;
        
        try{
            seq1.add(seq2);
            fail("Attempt to add a null sequence should result in an exception.");
        }catch(IllegalArgumentException e){  
        }
    }
    
    /**
     * Tests the "added sequence is the same object" case.
     */
    @Test
    public void testAddSame(){
        OperationSequence seq1 = new OperationSequence("+2");
        OperationSequence seq2 = seq1;
        
        try{
            seq1.add(seq2);
            fail("Attempt to add itself should result in an exception.");
        }catch(IllegalArgumentException e){        
        }
    }

    //--------------------------------------------------------------------------------------------------------------------
    
    /**
     * Tests method:
     * void Invert()
     * 
     * Test cases:
     * sequence contains operations
     * sequence doesn't contain operations
     */
    
    /**
     * Provides data for the
     * "sequence contains operations" test case.
     * 
     * @return test arguments
     */ 
    private static Stream<Arguments> testInvertFull() {
        return Stream.of(
            arguments("*2", "/2"),
            arguments("+2", "-2"),
            arguments("/54.00221671", "*54.00221671"),
            arguments("+1234567", "-1234567"),
            arguments("*2+3", "-3/2"),
            arguments("/5.3+2+1","-1-2*5.3"),
            arguments("+2+2+2+2+2", "-2-2-2-2-2"),
            arguments("-2*6/121.6+1","-1*121.6/6+2")
        );
    }   
    
    /**
     * Tests the
     * "sequence contains operations" case.
     * 
     * @param operations contents of sequence before inversion
     * @param expected contents of sequence after inversion
     */
    @ParameterizedTest
    @MethodSource
    public void testInvertFull(String operations, String expected){
        OperationSequence opSeqInverted = new OperationSequence(operations);
        OperationSequence opSeqExpected = new OperationSequence(expected);
        
        opSeqInverted.invert();
        
        assertEquals(opSeqExpected,opSeqInverted,
                "Result should have the numbers be in reverse order, preceded by opposite operators.");
    }
    
    /**
     * Tests the
     * "sequence doesn't contain conversions" case.
     */
    @Test
    public void testInvertEmpty(){
        OperationSequence opSeqEmpty = new OperationSequence();
        OperationSequence opSeqExpected = new OperationSequence();
        
        opSeqEmpty.invert();
        
        assertEquals(opSeqExpected,opSeqEmpty, 
                "Empty sequence should undergo no changes after inversion.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------

    /**
     * Tests method:
     * Operation get(int)
     * 
     * Test cases:
     * index is in bounds
     * index is out of bounds
     * sequence is empty
     */
    
    
    /**
     * Provides data for the
     * "index is in bounds" case.
     * 
     * @return test arguments
     */
    private static Stream<Arguments> testGetValidIndex() {
        return Stream.of(
            arguments("+1-2*3/4", 0, '+', 1),
            arguments("+1-2*3/4", 1, '-', 2),
            arguments("+1-2*3/4", 2, '*', 3),
            arguments("+1-2*3/4", 3, '/', 4),
            arguments("-1.22/0.002", 0, '-', 1.22),
            arguments("-1.22/0.002", 1, '/', 0.002)
        );
    }      
    
    /**
     * Tests the
     * "index is in bounds" case.
     * 
     * @param inputString contents of sequence
     * @param index
     * @param operator expected operator of returned Operation
     * @param value expected value of returned Operation
     */
    @ParameterizedTest
    @MethodSource
    public void testGetValidIndex(String inputString, int index, char operator, double value){
        OperationSequence seq = new OperationSequence(inputString);
        Operation expected = new Operation(operator, value);
        
        Operation operation = seq.get(index);
        
        assertEquals(expected, operation,
        "Returned operation should consist of the same operator and value that the sequence received at a given index.");
    }
    
    /**
     * Tests the
     * "index is out of bounds" case.
     * 
     * @return test arguments.
     */
    private static Stream<Arguments> testGetInvalidIndex() {
        return Stream.of(
            arguments("+1", 1),
            arguments("+1", -1),
            arguments("+1+2", 2),
            arguments("+1-2.22", 2)
        );
    }    
    
    /**
     * Tests the
     * "index is out of bounds" case.
     * 
     * @param inputString contents of sequence
     * @param index 
     */
    @ParameterizedTest
    @MethodSource
    public void testGetInvalidIndex(String inputString, int index){
        OperationSequence seq = new OperationSequence(inputString);
        
        try{
            Operation operation = seq.get(index);
            fail("Attempt to get an element outside the range should result in an exception.");
        }catch(IndexOutOfBoundsException e){           
        }
    }
    
    /**
     * Tests the "sequence is empty" case.
     */
    @Test
    public void testGetEmpty(){
        OperationSequence seq = new OperationSequence();
        try{
            Operation o1 = seq.get(0);
            fail("Attempt to get an element from an empty sequence should result in an exception.");
        }catch(IndexOutOfBoundsException e){   
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------   
    
    /**
     * Tests the toString() method.
     * 
     * @param operations contents of sequence
     */
    @ParameterizedTest
    @ValueSource(strings = { "+2", "+2.25", "+1-2*3/4",
                             "+0.000000000001", "-100000000000000",
                             "+1.23*4.56*7.89*10*11*12"})
    public void testToString(String operations){
        OperationSequence seq = new OperationSequence(operations);
        String output = seq.toString();
        assertEquals(operations, output,
                     "Sequence converted to string should be the same as the input.");
    }
    
    //--------------------------------------------------------------------------------------------------------------------
    
    private static Stream<Arguments> testEqualsValid() {
        return Stream.of(
            arguments("+1", "+1", true),
            arguments("+1.2", "+1.2", true),
            arguments("+1+2", "+1+2", true),
            arguments("+1", "+2", false),
            arguments("+1+2", "+1+2+3", false)
        );
    }    
    
    /**
     * Tests the equals(Object) method on valid input.
     * 
     * @param seq1 contents of first sequence.
     * @param seq2 contents of second sequence.
     * @param expected true if contents have the same value.
     */
    @ParameterizedTest
    @MethodSource
    public void testEqualsValid(String seq1, String seq2, Boolean expected){
        OperationSequence o1 = new OperationSequence(seq1);
        OperationSequence o2 = new OperationSequence(seq2);
        Boolean result = o1.equals(o2);
        assertEquals(expected, result, "Incorrect equals result for different objects.");
    }
    /**
     * Tests the equals(Object) method on same objects.
     */ 
    @Test
    public void testEqualsSame(){
        OperationSequence o1 = new OperationSequence();
        OperationSequence o2 = o1;
        Boolean result = o1.equals(o2);
        assertEquals(true, result, "Incorrect equals result for same objects.");
    }
    /**
     * Tests the equals(Object) method on comparison with null object.
     */ 
    @Test
    public void testEqualsNull(){
        OperationSequence o1 = new OperationSequence();
        OperationSequence o2 = null;
        Boolean result = o1.equals(o2);
        assertEquals(false, result, "Incorrect equals result for null object.");
    }
}
