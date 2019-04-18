package it.polimi.ingsw.server.model.cards;

/**
 * Represents a generic card with a {@linkplain #resourceId}.
 * The id is used to locate the resources associated with the specific card,
 * for example images and strings.
 *
 * @author Abbo Giulio A.
 */
public abstract class AbstractCard {
    /**
     * The id used to locate the resources associated with this instance.
     */
    private String resourceId;

    /**
     * This forces extending classes to set the id.
     *
     * @param resourceId the id to locate the resources for this object
     */
    AbstractCard(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the id to locate the resources for this object.
     *
     * @return the id to locate the resources for this object
     */
    public String getId() {
        return resourceId;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two cards are equal if they have the same {@linkplain #resourceId}.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as {@code obj}
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AbstractCard) &&
                ((AbstractCard) obj).resourceId.equals(resourceId);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return resourceId.hashCode();
    }
}