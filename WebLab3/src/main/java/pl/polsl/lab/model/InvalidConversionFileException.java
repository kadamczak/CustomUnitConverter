package pl.polsl.lab.model;

/**
 * Custom exception class used for situations when a line with incorrect syntax
 * or a duplicate conversion is found in "conversions.txt".
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class InvalidConversionFileException extends Exception{
    /** Index of last found incorrect line in "conversions.txt" file.*/
    private final int invalidLineIndex;
    
    /**
     * Constructor saving the error message and index of line where the problem occured.
     * 
     * @param errorMessage specifies what problem occured and on which line.
     * @param lineIndex index of last found incorrect line in "conversions.txt" file.
     */
    public InvalidConversionFileException(String errorMessage, int lineIndex) {
        super(errorMessage);
        this.invalidLineIndex = lineIndex;
    }
    
    /**
     * Returns index of last found incorrect line in "conversions.txt" file.
     * 
     * @return index of last incorrect line.
     */
    public int getInvalidLineIndex(){
        return this.invalidLineIndex;
    }
}
