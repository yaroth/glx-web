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
package ch.yaro.bls.rest.pojos;


import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is a simple POJO representation for a "Line" item stored in the "strecken" app in the 'strecke' repository.<br/>
 */
public class Line {

    public static final String WORKSPACE = "strecken";
    public static final String NODETYPE = "strecke";
    public static final String BASEPATH = "/";

    public static final String NAME = "name";
    public static final String ABSCHNITT = "abschnitt";

    private String name;
    private LinkedList<Section> fahrstrecke;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // MUST be a LinkedList
    public LinkedList<Section> getFahrstrecke() {
        return fahrstrecke;
    }

    public void setFahrstrecke(LinkedList<Section> fahrstrecke) {
        this.fahrstrecke = fahrstrecke;
    }

    /** Makes sure that a {@link Reservation} can be done on this Strecke.
     * Is a check whether departure and destination {@link Stop}s are in that
     * order in this Strecke.*/
    public boolean validateStreckenReservation(Reservation reservation) {
        String departure = reservation.getDeparture();
        String destination = reservation.getDestination();
        boolean isDepartureInStrecke = false;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Section section = (Section) stopIterator.next();
            if (!isDepartureInStrecke && section.getStopName().equals(departure)) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && section.getStopName().equals(destination)) {
                return true;
            }
        }
        return false;
    }

    /** Updates the reserved {@link Seat}s and the {@link Section}s reserved until the
     * next {@link Stop} on the whole Strecke.
     * @param reservation*/
    public void setTakenAbschnitteForReservation(Reservation reservation) {
        String departureLower = reservation.getDeparture().toLowerCase();
        String destinationLower = reservation.getDestination().toLowerCase();
        Integer wagenNumber = Integer.parseInt(reservation.getWagenNumber());
        Integer sitzNumber = Integer.parseInt(reservation.getSitzNumber());

        boolean isDepartureInStrecke = false;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Section section = (Section) stopIterator.next();
            String abschnittLower = section.getStopName().toLowerCase();
            if (!isDepartureInStrecke && abschnittLower.equals(departureLower)) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && abschnittLower.equals(destinationLower)) {
                isDepartureInStrecke = false;
            }
            if (isDepartureInStrecke) {
                section.setReservedTillNextStop(true);
                // adds Waggons with Seats to the Abschnitt
                if (section.containsWaggon(wagenNumber)) {
                    WaggonReservation waggonReservation = section.getWagenReservation(wagenNumber);
                    if (!waggonReservation.getReservedSeats().contains(sitzNumber)) {
                        waggonReservation.getReservedSeats().add(sitzNumber);
                    }
                } else {
                    section.getWaggonReservationList().add(new WaggonReservation(wagenNumber, sitzNumber));
                }
            }
        }
    }

    /**
     * Precondition: setTakenAbschnitteForReservation has been run on the Strecke!
     * Returns if a seat (Waggon and Seat) on a Strecke (departure, destination) is available
     * for reservation.
     * @param departure
     * @param destination
     * @param waggonNumber
     * @param seatNumber
     */
    public boolean seatIsAvailable(String departure, String destination, Integer waggonNumber, Integer seatNumber) {
        boolean isDepartureInStrecke = false;
        boolean seatIsAvailable = true;
        for (Iterator stopIterator = fahrstrecke.iterator(); stopIterator.hasNext(); ) {
            Section section = (Section) stopIterator.next();
            String abschnittLower = section.getStopName().toLowerCase();
            if (!isDepartureInStrecke && abschnittLower.equals(departure.toLowerCase())) {
                isDepartureInStrecke = true;
            }
            if (isDepartureInStrecke && abschnittLower.equals(destination.toLowerCase())) {
                isDepartureInStrecke = false;
            }
            if (isDepartureInStrecke) {
                if (section.isReservedTillNextStop()) {
                    if (section.containsWaggon(waggonNumber)) {
                        WaggonReservation wr = section.getWagenReservation(waggonNumber);
                        if (wr.getReservedSeats().contains(seatNumber)) {
                            seatIsAvailable = false;
                            return seatIsAvailable;
                        }
                    }
                }
            }
        }
        return seatIsAvailable;
    }

}
