package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;

public class Client extends User {

    private final String cashierId;
    private ClientType clientType;

    public Client(String name, String mail, String cashierId) {
        super(name, mail);
        if (!cashierId.matches("UW[0-9]{7}")) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.cashierId = cashierId;
        this.clientType = null;
    }

    public String getCashierId() {
        return this.cashierId;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    @Override
    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidAttributeException("Invalid id: cannot be null or empty");
        }

        id = id.trim().toUpperCase();

        if (id.matches("[XYZ]\\d{7}[A-Z]") || id.matches("\\d{8}[A-Z]")) // NIE o DNI
            this.clientType = ClientType.PERSON;
        else if (id.matches("[A-Z]\\d{8}")) // NIF
            this.clientType = ClientType.COMPANY;
        else
            throw new InvalidAttributeException("Invalid id: " + id);

        this.id = id;
    }

    @Override
    public String toString() {
        return "Client{identifier='" + this.getId() + "', name='" + this.getName() +
                "', email='" + this.getMail() + "', cash=" + this.cashierId + "}";
    }
}
