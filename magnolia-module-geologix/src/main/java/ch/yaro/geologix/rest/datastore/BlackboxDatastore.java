package ch.yaro.geologix.rest.datastore;

import ch.yaro.geologix.rest.pojos.Lunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is for demo reasony only. It has methods which pretend to read and write data from a "data store".
 * In fact the class isn't doing a lot more than mocking.
 */
public class BlackboxDatastore implements Store {

    private final Logger log = LoggerFactory.getLogger(BlackboxDatastore.class);

    /**
     * Stores a lunch in the data store.
     * Since this is just a mock, the method in fact does nothing real besides some logging.
     */
    public void add(Lunch lunch) throws Exception {
        // Emulating an error case.
        if (Lunch.BADFOOD.equals(lunch.getFood())) {
            String errorMsg = "Failed to store the lunch in the box due to bad food.";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        } else {
            log.info("Sucessfully stored some food in the box");
        }
    }

}
