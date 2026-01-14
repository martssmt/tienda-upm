package es.upm.etsisi.poo.app3.data.model.user;

import es.upm.etsisi.poo.app3.data.model.Entity;
import es.upm.etsisi.poo.app3.data.model.exceptions.InvalidAttributeException;
import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends Entity<String> {

    @Column(name = "username")
    private String name;
    @Column(name = "user_mail")
    private String mail;

    protected User() {
        super();
    }

    public User(String name, String mail) {
        super();
        this.name = name;
        if (!mail.endsWith("@upm.es")) {
            throw new InvalidAttributeException("Invalid mail address");
        }
        this.mail = mail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        if (!mail.endsWith("@upm.es")) {
            throw new InvalidAttributeException("Invalid mail address");
        }
        this.mail = mail;
    }

    @Override
    public abstract void setId(String id);

    @Override
    public String toString() {
        return "{class:User, id:" + this.getId() +
                ", name:'" + this.name + "', mail:'" + this.mail + "'}";
    }
}
