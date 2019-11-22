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


/**
 * This class is a simple POJO representation for a "Abschnitt" item stored in the "strecken" app in the 'strecke' repository.<br/>
 * Objects of this type are handy to create json on REST endpoints or within template model classes.
 */
public class Abschnitt {

    public static final String WORKSPACE = "strecken";
    public static final String NODETYPE = "mgnl:contentNode";
    public static final String BASEPATH = "/";

    public static final String STOP_ID = "stopID";
    public static final String STOP_DURATION = "stopduration";
    public static final String TRIP_DURATION = "tripduration";

    private String stopName;
    private int stopDuration;
    private int tripDuration;
    private boolean isReservedTillNextStop = false;

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }


    public int getStopDuration() {
        return stopDuration;
    }

    public void setStopDuration(int stopDuration) {
        this.stopDuration = stopDuration;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public boolean isReservedTillNextStop() {
        return isReservedTillNextStop;
    }

    public void setReservedTillNextStop(boolean reservedTillNextStop) {
        isReservedTillNextStop = reservedTillNextStop;
    }
}
