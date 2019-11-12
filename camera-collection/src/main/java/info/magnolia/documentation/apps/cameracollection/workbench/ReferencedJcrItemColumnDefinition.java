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
package info.magnolia.documentation.apps.cameracollection.workbench;

import info.magnolia.ui.workbench.column.definition.AbstractColumnDefinition;

/**
 * The definition class for {@link ReferencedJcrItemColumnFormatter}.
 */
public class ReferencedJcrItemColumnDefinition extends AbstractColumnDefinition {

    /**
     * The name of the workspace of the referenced item.
     */
    private String referencedItemWorkspace;
    /**
     * The name of the property of the referenced item.
     */
    private String referencedItemPropertyName;
    /**
     * The name of the property which holds the reference to the referenced item.
     */
    private String refItemPropertyName;


    public String getReferencedItemWorkspace() {
        return referencedItemWorkspace;
    }

    public void setReferencedItemWorkspace(String referencedItemWorkspace) {
        this.referencedItemWorkspace = referencedItemWorkspace;
    }

    public String getReferencedItemPropertyName() {
        return referencedItemPropertyName;
    }

    public void setReferencedItemPropertyName(String referencedItemPropertyName) {
        this.referencedItemPropertyName = referencedItemPropertyName;
    }

    public String getRefItemPropertyName() {
        return refItemPropertyName;
    }

    public void setRefItemPropertyName(String refItemPropertyName) {
        this.refItemPropertyName = refItemPropertyName;
    }
}
