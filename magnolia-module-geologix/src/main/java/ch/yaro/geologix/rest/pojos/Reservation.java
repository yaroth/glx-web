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


import java.rmi.server.RemoteServer;
import java.time.LocalDate;
import java.util.List;

/**
 * This class is a simple POJO representation for a "wagen" item stored in the "wagen" app.<br/>
 * Objects of this type are handy to create json on REST endpoints or within template model classes.
 */
public class Reservation extends NodeItem {

    public static final String WORKSPACE = "reservation";
    public static final String NODETYPE = "reservation";
    public static final String BASEPATH = "/";


    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String zugserviceID;
    private String wagenNumber;
    private String sitzNumber;
    private String from;
    private String to;



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getZugserviceID() {
        return zugserviceID;
    }

    public void setZugserviceID(String zugserviceID) {
        this.zugserviceID = zugserviceID;
    }

    public String getWagenNumber() {
        return wagenNumber;
    }

    public void setWagenNumber(String wagenNumber) {
        this.wagenNumber = wagenNumber;
    }

    public String getSitzNumber() {
        return sitzNumber;
    }

    public void setSitzNumber(String sitzNumber) {
        this.sitzNumber = sitzNumber;
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


}
