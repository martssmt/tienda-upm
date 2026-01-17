package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Abstract persistent user entity of the system.
 * <p>
 * This class represents a generic user and serves as a base class for all
 * specific user types (e.g. individual clients or company clients).
 * It is mapped as a JPA entity and participates in an inheritance hierarchy
 * using the {@link jakarta.persistence.InheritanceType#JOINED} strategy.
 * </p>
 *
 * <p>
 * Common user attributes such as {@code name} and {@code mail} are defined here.
 * The identifier is inherited from {@link Entity} and its concrete assignment
 * is delegated to subclasses.
 * </p>
 *
 * <p>
 * A basic validation rule is applied to the email address, requiring it to
 * belong to the {@code @upm.es} domain. Invalid values result in an
 * {@link es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException}.
 * </p>
 *
 * @author Jiling
 * @version 3.0
 * @see Entity
 */
@jakarta.persistence.Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends Entity<String> {

    /**
     * Username of the user.
     */
    @Column(name = "username")
    private String name;

    /**
     * Email address of the user.
     * <p>
     * The email must belong to the {@code @upm.es} domain.
     * </p>
     */
    @Column(name = "user_mail")
    private String mail;

    /**
     * Protected no-argument constructor required by JPA.
     */
    protected User() {
        super();
    }

    /**
     * Creates a new user with the given name and email address.
     *
     * @param name the user's name
     * @param mail the user's email address
     * @throws InvalidAttributeException if the email does not belong to
     *                                   the {@code @upm.es} domain
     */
    public User(String name, String mail) {
        super();
        this.name = name;
        if (!mail.endsWith("@upm.es")) {
            throw new InvalidAttributeException("Invalid mail address");
        }
        this.mail = mail;
    }

    /**
     * Returns the user's name.
     *
     * @return the username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the user's name.
     *
     * @param name the new username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's email address.
     *
     * @return the user email
     */
    public String getMail() {
        return this.mail;
    }

    /**
     * Sets the user's email address.
     *
     * <p>
     * The email must belong to the {@code @upm.es} domain.
     * </p>
     *
     * @param mail the new email address
     * @throws InvalidAttributeException if the email does not belong to
     *                                   the {@code @upm.es} domain
     */
    public void setMail(String mail) {
        if (!mail.endsWith("@upm.es")) {
            throw new InvalidAttributeException("Invalid mail address");
        }
        this.mail = mail;
    }

    /**
     * Assigns the identifier of the user.
     * <p>
     * The concrete implementation is delegated to subclasses, allowing
     * different identifier formats or generation policies.
     * </p>
     *
     * @param id the identifier to be assigned
     */
    @Override
    public abstract void setId(String id);

    /**
     * Returns a string representation of the user.
     *
     * @return a textual representation of the user
     */
    @Override
    public String toString() {
        return "{class:User, id:" + this.getId() +
                ", name:'" + this.name + "', mail:'" + this.mail + "'}";
    }
}