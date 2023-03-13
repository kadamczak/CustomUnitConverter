/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.lab.model;

import java.io.Serializable;

/**
 * Compound key for the "Conversion" class.
 * Consists of originalUnit and targetUnit.
 * 
 * @author Kinga Adamczak
 * @version 1.0
 */
public class ConversionID implements Serializable{
    /** Symbol of original unit. */
    private String originalUnit;
    /** Symbol of target unit. */
    private String targetUnit;

    /**
     * Constructor for setting unit symbols.
     * 
     * @param original symbol of original unit
     * @param target symbol of target unit
     */
    public ConversionID(String original, String target) {
        this.originalUnit = original;
        this.targetUnit = target;
    }

    public String getOriginalUnit() {
        return originalUnit;
    }

    public String getTargetUnit() {
        return targetUnit;
    }
    
    public void setOriginalUnit(String original){
        this.originalUnit = original;
    }
    
    public void setTargetUnit(String target){
        this.targetUnit = target;
    }
}
