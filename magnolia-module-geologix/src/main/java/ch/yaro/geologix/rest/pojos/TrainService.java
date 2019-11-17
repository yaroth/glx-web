package ch.yaro.geologix.rest.pojos;


import java.time.LocalTime;
import java.util.LinkedList;

public class TrainService extends NodeItem {

    public static final String WORKSPACE = "zugservices";
    public static final String NODETYPE = "zugservice";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String DEPARTURE = "departure";
    public static final String STRECKE = "strecke";
    public static final String ZUGKOMPOSITION = "zugkomposition";


    private String name;
    private String departure;
    private String streckeID;
    private LinkedList<Stop> timetable;
    private String zugkompositionID;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getStreckeID() {
        return streckeID;
    }

    public void setStreckeID(String streckeID) {
        this.streckeID = streckeID;
    }

    public String getZugkompositionID() {
        return zugkompositionID;
    }

    public void setZugkompositionID(String zugkompositionID) {
        this.zugkompositionID = zugkompositionID;
    }

    public LinkedList<Stop> getTimetable() {
        return timetable;
    }

    public void setTimetable(LinkedList<Stop> timetable) {
        this.timetable = timetable;
    }
}
