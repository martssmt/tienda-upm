package es.upm.etsisi.poo.app3.data.model.user;

/**
 * The {@code ClientType} enumeration represents the different types of clients
 * supported by the store application.
 * <p>
 * It is used to distinguish between individual clients and company clients,
 * enabling the application of specific business rules depending on the
 * client category.
 * </p>
 *
 * <p>
 * Client type plays a key role in ticket creation and validation, especially
 * when determining whether products, services, or combined tickets are allowed.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ClientType type = ClientType.COMPANY;
 * if (type == ClientType.COMPANY) {
 *     // apply company-specific rules
 * }
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum ClientType {

    /**
     * Individual (personal) client.
     */
    PERSON,

    /**
     * Company client.
     */
    COMPANY
}
