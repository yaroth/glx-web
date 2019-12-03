package ch.yaro.geologix.rest.pojos;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jcr.RepositoryException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

public class TrainService extends NodeItem {

    public static final String WORKSPACE = "zugservices";
    public static final String NODETYPE = "zugservice";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String DEPARTURE = "departure";
    public static final String STRECKE_ID = "strecke";
    public static final String ZUGKOMPOSITION_ID = "zugkomposition";


    private String name;
    private String departure;
    private String arrival;
    private String from;
    private String to;
    @JsonIgnore
    private String streckeID;
    private LinkedList<Stop> timetable;
    @JsonIgnore
    private String zugkompositionID;
    private LinkedList<Wagen> zugkomposition;



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

    public LinkedList<Wagen> getZugkomposition() {
        return zugkomposition;
    }

    public void setZugkomposition(LinkedList<Wagen> zugkomposition) {
        this.zugkomposition = zugkomposition;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public boolean fitsRequest(TrainServiceRequest request) {
        LocalTime earliestDeparture = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
        String startStop = request.getFrom();
        String endStop = request.getTo();
        boolean startStopFitsDeparture = false;
        for (Iterator stopIterator = timetable.iterator(); stopIterator.hasNext(); ) {
            Stop stop = (Stop) stopIterator.next();
            if (!startStopFitsDeparture && stop.getStopName().equals(startStop) && stop.getTimeOut().isAfter(earliestDeparture)) {
                startStopFitsDeparture = true;
            }
            if (startStopFitsDeparture && stop.getStopName().equals(endStop)) {
                return true;
            }
        }
        return false;
    }

    public void adaptTimetableToRequest(TrainServiceRequest request) throws RepositoryException {
        String startStop = request.getFrom();
        String endStop = request.getTo();
        LinkedList<Stop> updatedTimetable = new LinkedList<>();
        boolean startAddingStops = false;
        for (Iterator stopIterator = timetable.iterator(); stopIterator.hasNext(); ) {
            Stop stop = (Stop) stopIterator.next();
            if (!startAddingStops && stop.getStopName().equals(startStop)) {
                stop.setTimeIN(null);
                startAddingStops = true;
                setFrom(startStop);
                int minutes = stop.getTimeOut().getMinute();
                String minutesPlaceholder = "";
                if (minutes < 10) minutesPlaceholder = "0";
                setDeparture(stop.getTimeOut().getHour() + ":" + minutesPlaceholder + minutes);
            }
            if (startAddingStops){
                updatedTimetable.add(stop);
            }
            if (stop.getStopName().equals(endStop)) {
                stop.setTimeOut(null);
                setTo(endStop);
                int minutes = stop.getTimeIN().getMinute();
                String minutesPlaceholder = "";
                if (minutes < 10) minutesPlaceholder = "0";
                setArrival(stop.getTimeIN().getHour() + ":" + minutesPlaceholder + minutes);
                break;
            }
        }
        timetable = updatedTimetable;
    }

    public Wagen getWagenByNumber (String wagenNumber) {
        for (Iterator wagenIterator = zugkomposition.iterator(); wagenIterator.hasNext();) {
            Wagen w = (Wagen) wagenIterator.next();
            if (w.getNumber().equals(wagenNumber)) return w;
        }
        return null;
    }

}
