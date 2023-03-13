package pl.polsl.lab.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Class for automatic conversion between OperationSequence (class used in application logic)
 * and a String (text representation used in the database).
 * 
 * @author Kinga Adamczak
 * @version 1.0
 */
@Converter(autoApply=true)
public class OperationSequenceConverter implements AttributeConverter<OperationSequence, String> {

    @Override
    public String convertToDatabaseColumn(OperationSequence attribute) {
        return attribute.toString();
    }

    @Override
    public OperationSequence convertToEntityAttribute(String dbData) {
       return new OperationSequence(dbData);
    }
    
}