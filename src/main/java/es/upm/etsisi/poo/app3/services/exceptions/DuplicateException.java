package es.upm.etsisi.poo.app3.services.exceptions;

public class DuplicateException extends RuntimeException {
    private static final String DESCRIPTION = "Duplicate Exception. Attribute duplicated, must be unique";

    public DuplicateException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}