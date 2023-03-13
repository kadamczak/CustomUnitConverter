package pl.polsl.lab.model;

/**
 * This package contains the ConversionContainer class, whose instance serves
 * as the database of the program. Other classes defined here operate on it either
 * for file reading/writing or for performing conversions between two units.
 * 
 * Conversion class and its underlying OperationSequence class serve as building blocks
 * for the ConversionContainer.
 * 
 * Main output of the package are ValueWithUnit class instances, as they are the result
 * of conversion performed by the Converter class. Converter is capable of converting units
 * that don't have a direct connection.
 * 
 * This package serves the role of a Model.
 */
