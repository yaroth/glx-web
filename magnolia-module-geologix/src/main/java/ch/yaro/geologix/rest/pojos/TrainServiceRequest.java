package ch.yaro.geologix.rest.pojos;

import javax.print.DocFlavor;
import java.sql.Time;
import java.time.LocalTime;

public class TrainServiceRequest extends NodeItem {

    private String time;
    private String from;
    private String to;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public String toString(){
        return "From: " + from + " to: " + to + ", departure @" + time;
    }
}
