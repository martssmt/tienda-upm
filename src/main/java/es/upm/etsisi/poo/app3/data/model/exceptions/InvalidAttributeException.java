package es.upm.etsisi.poo.app3.data.model.exceptions;

public class InvalidAttributeException extends RuntimeException {
    private static final String DESCRIPTION = "Invalid Attribute Exception. The attribute value is out of range.";

    public InvalidAttributeException(String detail) {
        super(DESCRIPTION + " " + detail);
    }
}
