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


import java.util.List;

/**
 * This class is a simple POJO representation for a "wagen" item stored in the "wagen" app.<br/>
 * Objects of this type are handy to create json on REST endpoints or within template model classes.
 */
public class Wagen extends NodeItem {

    public static final String WORKSPACE = "wagen";
    public static final String NODETYPE = "wagen";
    public static final String BASEPATH = "/";

    public static final String NUMBER = "number";
    public static final String WAGENPLAN = "wagenplan";
    public static final String WAGENTYP = "wagentyp";

    private String number;
    private String wagenplanID;
    private List<String> wagentypIDs;

    public List<String> getWagentypIDs() {
        return wagentypIDs;
    }

    public void setWagentypIDs(List<String> wagentypIDs) {
        this.wagentypIDs = wagentypIDs;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public void setWagenplanID(String wagenplanID) {
        this.wagenplanID = wagenplanID;
    }

    public String getWagenplanID() {
        return wagenplanID;
    }
}