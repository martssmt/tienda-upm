package es.upm.etsisi.poo.app3.data.model.shop;

/**
 * The {@code ServiceType} enumeration defines the different categories of
 * services available in the store system.
 * <p>
 * Each service type represents a specific kind of service that can be
 * contracted by company clients and later processed for billing.
 * </p>
 *
 * <p>
 * Service types are particularly relevant in company tickets, where services
 * may be included alone or combined with products, influencing ticket
 * validation and discount rules.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ServiceType type = ServiceType.TRANSPORT;
 * // apply service-specific logic
 * }</pre>
 *
 * @author Jiling
 * @version 3.0
 */
public enum ServiceType {

    /**
     * Insurance-related service.
     */
    INSURANCE,

    /**
     * Transport-related service.
     */
    TRANSPORT,

    /**
     * Show or event-related service.
     */
    SHOWS
}
