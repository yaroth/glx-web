package ch.yaro.geologix.rest.pojos;

import java.util.UUID;

/**
 * A base class for "storable" pojos which must have a least an id.
 */
public class BasicStorable {

    private String id;

    public BasicStorable(){
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }


}
