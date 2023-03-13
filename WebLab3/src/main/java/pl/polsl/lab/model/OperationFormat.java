package pl.polsl.lab.model;
import java.util.regex.Pattern;

/**
 * Class containing regex used for parsing operations from
 * the command line and "conversions.txt" file.
 * 
 * @version 2.0
 * @author Kinga Adamczak
 */
public class OperationFormat {
    /** 
     * Regex for operations required to convert value measured in one unit to another.
     * Ensures that the string consists of operators immediately followed by numeric values.
     * Does not accept operations including only zeroes.
     */
    private final Pattern opSequenceRegex;
    
    /**
     * Constructor initializing regex pattern.
     */
    public OperationFormat(){
        opSequenceRegex = Pattern.compile("^([\\+\\-\\*\\/][1-9]+\\d*(\\.\\d+)?|[\\+\\-\\*\\/]0\\.(0*[1-9]+0*){1,}){1,}$");
    }
    
    /**
     * Returns regex for operation sequences.
     * 
     * @return instance of class Pattern using the operation sequence regex.
     */
    public Pattern getOpSequenceRegex(){
        return this.opSequenceRegex;
    }
    
    /**
     * Checks if entered text matches the regex.
     * 
     * @param text entered text.
     * @return true if text matches regex
     */
    public Boolean checkCorrectness(String text){
        if(text == null)
            throw new IllegalArgumentException("Tested string was null.");
        return text.matches(opSequenceRegex.pattern());
    }
}
