/**
 * This file Copyright (c) 2014 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is licensed under the MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package ch.yaro.geologix.rest.pojos;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a simple POJO representation for a "Strecke" item stored in the "strecken" app in the 'strecke' repository.<br/>
 * Objects of this type are handy to create json on REST endpoints or within template model classes.
 */
public class Strecke {

    public static final String WORKSPACE = "strecken";
    public static final String NODETYPE = "strecke";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String ABSCHNITT = "abschnitt";

    private String name;
    private LinkedList<Abschnitt> fahrstrecke;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // MUST be a LinkedList
    public LinkedList<Abschnitt> getFahrstrecke() {
        return fahrstrecke;
    }

    public void setFahrstrecke(LinkedList<Abschnitt> fahrstrecke) {
        this.fahrstrecke = fahrstrecke;
    }

    public boolean validateStreckenReservation(Reservation reservation) {
        String departure = reservation.getDeparture();
        String destination = reservation.getDestination();
        boolean isDepartureInStrecke = false;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Abschnitt abschnitt = (Abschnitt) stopIterator.next();
            if (!isDepartureInStrecke && abschnitt.getStopName().equals(departure)) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && abschnitt.getStopName().equals(destination)) {
                return true;
            }
        }
        return false;
    }


    public void setTakenAbschnitte(Reservation reservation) {
        String departure = reservation.getDeparture();
        String destination = reservation.getDestination();
        boolean isDepartureInStrecke = false;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Abschnitt abschnitt = (Abschnitt) stopIterator.next();
            if (!isDepartureInStrecke && abschnitt.getStopName().equals(departure)) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && abschnitt.getStopName().equals(destination)) {
                isDepartureInStrecke = false;
            }
            if (isDepartureInStrecke){
                abschnitt.setReservedTillNextStop(true);
            }
        }
    }

    public boolean checkSeatAvailability(Reservation reservation) {
        String departure = reservation.getDeparture();
        String destination = reservation.getDestination();
        boolean isDepartureInStrecke = false;
        boolean seatIsAvailable = true;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Abschnitt abschnitt = (Abschnitt) stopIterator.next();
            if (!isDepartureInStrecke && abschnitt.getStopName().equals(departure)) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && abschnitt.getStopName().equals(destination)) {
                isDepartureInStrecke = false;
            }
            if (isDepartureInStrecke){
                if (abschnitt.isReservedTillNextStop()) {
                    seatIsAvailable = false;
                    return seatIsAvailable;
                }
            }
        }
        return seatIsAvailable;
    }
}
