package ch.yaro.bls.rest.pojos;


import java.util.LinkedList;

/**
 * This class is a simple POJO representation for a "TrainComposition" item stored in the "zugkompositionen"
 * app in the 'zugkomposition' repository.<br/>
 * Mainly contains a list (LinkedList!) of {@link Waggon}.
 */
public class TrainComposition {

    public static final String WORKSPACE = "zugkompositionen";
    public static final String NODETYPE = "zugkomposition";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String WAEGEN = "waegen";

    private String name;
    private LinkedList<Waggon> waggonList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Waggon> getWagenList() {
        return waggonList;
    }

    public void setWagenList(LinkedList<Waggon> waggonList) {
        this.waggonList = waggonList;
    }
}
