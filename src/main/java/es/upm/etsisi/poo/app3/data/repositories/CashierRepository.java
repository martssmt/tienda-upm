package es.upm.etsisi.poo.app3.data.repositories;

import es.upm.etsisi.poo.app3.data.model.shop.ticket.Ticket;
import es.upm.etsisi.poo.app3.data.model.user.Cashier;

import java.util.List;

/**
 * Repository interface for {@link Cashier} entities.
 * <p>
 * This interface defines persistence operations specific to cashiers,
 * extending the generic {@link RepositoryUser} contract. In addition to
 * standard user-related operations, it provides access to the tickets
 * managed within the system.
 * </p>
 *
 * <p>
 * Concrete implementations (e.g. Hibernate-based repositories) are responsible
 * for defining how these operations are executed against the persistence layer.
 * </p>
 *
 * @author Marta
 * @version 3.0
 * @see Cashier
 * @see Ticket
 * @see RepositoryUser
 */
public interface CashierRepository extends RepositoryUser<Cashier> {

    /**
     * Retrieves all tickets stored in the system.
     *
     * @return the list of tickets
     */
    List<Ticket> listTickets();

    /**
     * Persists a new cashier entity.
     * <p>
     * Implementations may apply additional logic such as identifier generation
     * before persistence.
     * </p>
     *
     * @param cashier the cashier to persist
     */
    void add(Cashier cashier);
}
