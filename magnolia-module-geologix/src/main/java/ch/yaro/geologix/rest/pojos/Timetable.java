/**
 * This file Copyright (c) 2014 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 * <p>
 * <p>
 * This file is licensed under the MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.yaro.geologix.rest.pojos;


import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a simple POJO representation for a "Strecke" item stored in the "strecken" app in the 'strecke' repository.<br/>
 * Objects of this type are handy to create json on REST endpoints or within template model classes.
 */
public class Timetable {

    // Must be implemented as a LinkedList
    private LinkedList<Stop> timetable = new LinkedList<>();


    public Timetable(Strecke strecke, LocalTime departureTime) {
        if (strecke != null) {
            LocalTime tempTime = departureTime;
            for (Iterator abschnittIterator = strecke.getFahrstrecke().iterator(); abschnittIterator.hasNext(); ) {
                Abschnitt abschnitt = (Abschnitt) abschnittIterator.next();
                Stop stop = new Stop(abschnitt.getStopName(), tempTime, tempTime.plusMinutes(abschnitt.getStopDuration()));
                tempTime = tempTime.plusMinutes(abschnitt.getTripDuration());
                timetable.add(stop);
            }
            timetable.getFirst().setTimeIN(null);
            timetable.getLast().setTimeOut(null);
        }
    }




    public LinkedList<Stop> getTimetable() {
        return timetable;
    }

    public void setTimetable(LinkedList<Stop> timetable) {
        this.timetable = timetable;
    }


}
