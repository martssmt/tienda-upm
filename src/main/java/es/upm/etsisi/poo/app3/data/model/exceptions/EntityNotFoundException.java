package es.upm.etsisi.poo.app3.data.model.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private static final String DESCRIPTION = "Entity Not Found Exception";

    public EntityNotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}