package pl.polsl.lab.model;

import java.util.HashMap;
import java.util.Set;

/**
 * Class containing metric prefixes and their multipliers
 * stored as a map.
 * 
 * @version 1.0
 * @author Kinga Adamczak
 */
public class MetricPrefixesMap {
    /** Map with metric prefixes as keys and multipliers as their
       corresponding values. */
    private final HashMap<String, String> data;
    
    /**
     * Constructor initializing keys and values of the map.
     */
    public MetricPrefixesMap(){
        data = new HashMap<>();
        
        data.put("T", "*0.0000000000000001");
        data.put("G", "*0.000000001");
        data.put("M", "*0.000001");
        data.put("k", "*0.001");
        data.put("h", "*0.01");
        data.put("da", "*0.1");
        data.put("d", "*10");
        data.put("c", "*100");
        data.put("m", "*1000");
        data.put("μ", "*1000000");
        data.put("n", "*1000000000");
        data.put("p", "*10000000000000000");
    }
    
    /**
     * Gets prefixes from the map.
     * 
     * @return Set of map's keys (prefixes).
     */
    public Set<String> keySet(){
        return data.keySet();
    }
    
    /**
     * Gets multiplier corresponding to a prefix.
     * 
     * @param key prefix.
     * @return multiplier associated with the prefix
     *         stored as a String.
     */
    public String returnMultiplier(String key){
        return data.get(key);
    }
}
