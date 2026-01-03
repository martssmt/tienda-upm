package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;

public class Client extends User {
    private String cashierId;
    private final ClientType clientType;

    public Client(String name, String mail, String cashierId,  ClientType clientType) {
        super(name, mail);
        if (!cashierId.matches("UW[0-9]{7}")) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.cashierId = cashierId;
        this.clientType = clientType;
    }

    public String getCashierId() {
        return this.cashierId;
    }

    public void setCashierId(String cashierId) {
        if (!cashierId.matches("UW[0-9]{7}")) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.cashierId = cashierId;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    @Override
    public void setId(String id) {
        if(clientType == null){
            throw new InvalidAttributeException("ClientType is null");
        }

        switch(clientType) {
            case COMPANY:
                if (!id.matches("[A-Za-z][0-9]{8}")) {
                    throw new InvalidAttributeException("Invalid NIF for company");
                }
                break;
            case PERSON:
                if (id.length() != 9) {
                    throw new InvalidAttributeException("Invalid DNI");
                }
                break;
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "Client{identifier='" + this.getId() + "', name='" + this.getName() +
                "', email='" + this.getMail() + "', cash=" + this.cashierId + "}";
    }
}
