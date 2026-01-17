package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Client} class represents a persistent client of the store system.
 * <p>
 * A client is a concrete specialization of {@link User} and may represent
 * either an individual person or a company, depending on the format of its
 * identifier (DNI/NIE or NIF).
 * </p>
 *
 * <p>
 * Each client is associated with a cashier identifier and maintains a list
 * of ticket identifiers corresponding to the tickets created by the client.
 * </p>
 *
 * <p>
 * The client type ({@link ClientType}) is automatically inferred when the
 * identifier is set, enforcing the business rules defined for personal and
 * company clients.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * Client client = new Client("Alice", "alice@upm.es", "UW1234567");
 * client.setId("12345678A"); // personal client
 * client.addTicket("T1");
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 * @see User
 * @see ClientType
 */
@jakarta.persistence.Entity
@Table(name = "clients")
public class Client extends User {

    /**
     * Identifier of the cashier associated with the client.
     */
    @Column(name = "cashierId")
    private String cashierId;

    /**
     * Type of the client (PERSON or COMPANY).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType clientType;

    /**
     * List of ticket identifiers associated with the client.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "client_tickets",
            joinColumns = @JoinColumn(name = "client_id")
    )
    @Column(name = "ticket_list")
    private List<String> ticketIds;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected Client() {
        super();
    }

    /**
     * Creates a new client with the given name, email, and cashier identifier.
     *
     * @param name      the client's name
     * @param mail      the client's email address
     * @param cashierId the associated cashier identifier
     * @throws InvalidAttributeException if the cashier identifier format is invalid
     */
    public Client(String name, String mail, String cashierId) {
        super(name, mail);
        if (!cashierId.matches("UW[0-9]{7}")) {
            throw new InvalidAttributeException("Invalid cashierId");
        }
        this.cashierId = cashierId;
        this.clientType = null;
        this.ticketIds = new ArrayList<>();
    }

    /**
     * Returns the cashier identifier associated with the client.
     *
     * @return the cashier identifier
     */
    public String getCashierId() {
        return this.cashierId;
    }

    /**
     * Returns the client type.
     *
     * @return the client type
     */
    public ClientType getClientType() {
        return this.clientType;
    }

    /**
     * Returns the list of ticket identifiers associated with the client.
     *
     * @return the list of ticket identifiers
     */
    public List<String> getTicketIds() {
        return ticketIds;
    }

    /**
     * Assigns the identifier of the client and determines its type.
     * <p>
     * The identifier format is validated and used to infer whether the client
     * is a personal client (DNI/NIE) or a company client (NIF).
     * </p>
     *
     * @param id the identifier to be assigned
     * @throws InvalidAttributeException if the identifier is invalid
     */
    @Override
    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidAttributeException("Invalid id: cannot be null or empty");
        }

        id = id.trim().toUpperCase();

        if (id.matches("[XYZ]\\d{7}[A-Z]") || id.matches("\\d{8}[A-Z]"))
            this.clientType = ClientType.PERSON;
        else if (id.matches("[A-Z]\\d{8}"))
            this.clientType = ClientType.COMPANY;
        else
            throw new InvalidAttributeException("Invalid id: " + id);

        this.id = id;
    }

    /**
     * Adds a ticket identifier to the client's list of tickets.
     *
     * @param ticketId the ticket identifier to add
     */
    public void addTicket(String ticketId) {
        this.ticketIds.add(ticketId);
    }

    /**
     * Returns a string representation of the client.
     *
     * @return a textual representation of the client
     */
    @Override
    public String toString() {
        String type = (this.clientType == ClientType.COMPANY) ? "COMPANY" : "USER";
        return type + "{identifier='" + this.getId() + "', name='" + this.getName() +
                "', email='" + this.getMail() + "', cash=" + this.cashierId + "}";
    }
}