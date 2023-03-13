package pl.polsl.lab.model;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;

/**
 * Class for storing all information about a particular unit.
 * 
 * @author Kinga Adamczak
 * @version 1.0
 */
@Entity
@Table(name = "UNITS")
public class Unit implements Serializable{
    /** Symbol of unit (also the ID). */
    @Id
    @OneToMany(cascade=CascadeType.REMOVE)
    private final String symbol;
    /** Full name (for example "Celsius" for C). */
    private String fullName;
    /** Measuring system. */
    private String system;

    @Transient
    /** Specifies maximum length of text values. */
    private final int MAX_LENGTH;
    
    /**
     * Constructor initializing all fields of the class.
     * 
     * @param unitSymbol symbol, having the role of the unit ID
     * @param unitName full name
     * @param unitSystem measuring system
     */
    public Unit(String unitSymbol, String unitName, String unitSystem){
        this.symbol = unitSymbol;
        this.fullName = unitName;
        this.system = unitSystem;
        this.MAX_LENGTH = 255;
    }
    
    /**
     * Constructor filling the symbol with a random ID, and the additional fields with "Unknown"
     */
    public Unit(){
        this(UUID.randomUUID().toString(), "Unknown", "Unknown");
    }
    
    /**
     * Constructor for an unit with a specified symbol, fills the additional fields with "Unknown"
     */
    public Unit(String unitSymbol){
        this(unitSymbol, "Unknown", "Unknown");
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSystem() {
        return system;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.symbol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unit other = (Unit) obj;
        return Objects.equals(this.symbol, other.symbol);
    }
    
    
    
}