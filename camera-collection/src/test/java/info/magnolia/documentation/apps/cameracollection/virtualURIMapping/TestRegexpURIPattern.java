/**
 * This file Copyright (c) 2017 Magnolia International
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
package info.magnolia.documentation.apps.cameracollection.virtualURIMapping;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import info.magnolia.cms.beans.config.RegexpVirtualURIMapping;
import info.magnolia.cms.beans.config.VirtualURIMapping;

import org.junit.Test;

/**
 * A simple test class to figure out some proper regexp pattern for a RegexpVirtualURIMapping based virrtual URI mapping.
 * (Also use https://regex101.com/ to fiddle around with those pattern. Can be a big help!)
 */
public class TestRegexpURIPattern {

    /**
     * Test the regexp pattern to redirect "/json/cameras/F3" to "/json?path=/F3".
     */
    @Test
    public void testRegexpVirtualURIMapping_A() throws Exception {
        // GIVEN
        String incomingURI = "/json/cameras/F3";
        //
        String fromURI = "/json/cameras(\\/[0-9A-Z]+)";
        String forwardToURI = "forward:/json?path=$1";
        //
        VirtualURIMapping vm = createRegexpVirtualURIMapping(fromURI, forwardToURI);

        // WHEN
        final VirtualURIMapping.MappingResult result = vm.mapURI(incomingURI);

        // THEN
        assertNotNull(result);
        assertEquals("forward:/json?path=/F3", result.getToURI());

    }

    /**
     * Test the regexp pattern to redirect "/getjson/cameras/F3" to "/json?path=/cameras/F3".
     */
    @Test
    public void testRegexpVirtualURIMapping_B() throws Exception {
        // GIVEN
        String incomingURI = "/getjson/cameras/F3";
        //
        String fromURI = "/getjson([0-9A-Z-a-z-\\/]+)";
        String forwardToURI = "forward:/getjson?path=$1";
        //
        VirtualURIMapping vm = createRegexpVirtualURIMapping(fromURI, forwardToURI);

        // WHEN
        final VirtualURIMapping.MappingResult result = vm.mapURI(incomingURI);

        // THEN
        assertNotNull(result);
        assertEquals("forward:/getjson?path=/cameras/F3", result.getToURI());
    }

    /**
     * Test the regexp pattern to redirect "/getjson/cameracollection" to "/json?workspace=cameracollection".
     */
    @Test
    public void testRegexpVirtualURIMapping_C() throws Exception {
        // GIVEN
        String incomingURI = "/getjson/cameracollection";
        //
        String fromURI = "/getjson/([0-9A-Z-a-z]+)";
        String forwardToURI = "forward:/getjson?workspace=$1";
        //
        VirtualURIMapping vm = createRegexpVirtualURIMapping(fromURI, forwardToURI);

        // WHEN
        final VirtualURIMapping.MappingResult result = vm.mapURI(incomingURI);

        // THEN
        assertNotNull(result);
        assertEquals("forward:/getjson?workspace=cameracollection", result.getToURI());
    }

    /**
     * Test the regexp pattern to redirect "/getjson/cameracollection/cameras/F3" to "/json?workspace=cameracollection&path=/cameras/F3".
     */
    @Test
    public void testRegexpVirtualURIMapping_D() throws Exception {
        // GIVEN
        String incomingURI = "/getjson/cameracollection/cameras/F3";
        //
        String fromURI = "/getjson/([0-9A-Z-a-z]+)([0-9A-Z-a-z-\\/]+)";
        String forwardToURI = "forward:/getjson?workspace=$1&path=$2";
        //
        VirtualURIMapping vm = createRegexpVirtualURIMapping(fromURI, forwardToURI);

        // WHEN
        final VirtualURIMapping.MappingResult result = vm.mapURI(incomingURI);

        // THEN
        assertNotNull(result);
        assertEquals("forward:/getjson?workspace=cameracollection&path=/cameras/F3", result.getToURI());
    }


    private RegexpVirtualURIMapping createRegexpVirtualURIMapping(String fromURI, String forwardTo){
        RegexpVirtualURIMapping vm = new RegexpVirtualURIMapping();
        vm.setFromURI(fromURI);
        vm.setToURI(forwardTo);
        return vm;
    }


}
