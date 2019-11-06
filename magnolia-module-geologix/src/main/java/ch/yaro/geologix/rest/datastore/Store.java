package ch.yaro.geologix.rest.datastore;


import ch.yaro.geologix.rest.pojos.Lunch;

/**
 * A simple interface for a generic data store which can handle a few types such as {@link Lunch}.
 */
public interface Store {

    /**
     * Stores a lunch in the data store.
     */
    void add(Lunch lunch) throws Exception;

}
