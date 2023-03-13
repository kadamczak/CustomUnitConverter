package pl.polsl.lab.model;

import java.util.regex.Pattern;

/**
 * Class containing regex for units that 
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class NoPrefixUnitFormat {
    /** Regex for metric units. Ensures that the base unit doesn't have a prefix. */
    private final Pattern noPrefixRegex;
    
    /**
     * Constructor initializing regex pattern.
     */
    public NoPrefixUnitFormat(){
        noPrefixRegex = Pattern.compile("^([T|G|M|k|h|d|c|m|μ|n|p]|da|(?<!da)[^T|G|M|k|h|d|c|m|μ|n|p|\\W][\\w]*)$");
    }
    
    /**
     * Returns regex for no prefix units.
     * 
     * @return instance of class Pattern using the no prefix unit regex.
     */
    public Pattern getNoPrefixRegex(){
        return this.noPrefixRegex;
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
        return text.matches(noPrefixRegex.pattern());
    }
}
