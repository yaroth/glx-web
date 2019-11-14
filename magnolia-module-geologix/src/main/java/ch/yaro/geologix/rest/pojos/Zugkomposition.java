package ch.yaro.geologix.rest.pojos;


import java.util.List;

public class Zugkomposition {

    public static final String WORKSPACE = "zugkompositionen";
    public static final String NODETYPE = "zugkomposition";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String WAEGEN = "waegen";

    private String name;
    private List<Wagen> wagenList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Wagen> getWagenList() {
        return wagenList;
    }

    public void setWagenList(List<Wagen> wagenList) {
        this.wagenList = wagenList;
    }
}
