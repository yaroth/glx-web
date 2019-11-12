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

import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrItemId;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import info.magnolia.ui.workbench.column.AbstractColumnFormatter;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.ui.Table;

/**
 * A custom {@link info.magnolia.ui.workbench.column.definition.ColumnFormatter} to render a property of a referenced item.
 * (For instance an item in content app1 has a reference to an item in content app app2.<br/>
 * This column formatter is used in content app1 to render a property of the referenced item from content app2.<br/>
 */
public class ReferencedJcrItemColumnFormatter extends AbstractColumnFormatter<ReferencedJcrItemColumnDefinition> {

    private final Logger log = LoggerFactory.getLogger(ReferencedJcrItemColumnFormatter.class);

    public ReferencedJcrItemColumnFormatter(ReferencedJcrItemColumnDefinition definition) {
        super(definition);
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {

        StringBuilder res = new StringBuilder("");
        res.append("<span>");

        if (itemId instanceof JcrItemId) {
            Item item = source.getItem(itemId);

            String referencedPropertyValue = null;
            if (StringUtils.isNotBlank(definition.getRefItemPropertyName()) && StringUtils.isNotBlank(definition.getReferencedItemWorkspace()) && StringUtils.isNotBlank(definition.getReferencedItemPropertyName())) {
                String refItemUuid = String.valueOf(item.getItemProperty(definition.getRefItemPropertyName()));
                if (StringUtils.isNotBlank(refItemUuid)) {
                    Node referencedNode = null;
                    try {
                        referencedNode = NodeUtil.getNodeByIdentifier(definition.getReferencedItemWorkspace(), refItemUuid);
                        referencedPropertyValue = PropertyUtil.getString(referencedNode, definition.getReferencedItemPropertyName());
                    } catch (RepositoryException e) {
                        log.error("Failed to detch referenced node for ws={} and uuid={}", e, definition.getReferencedItemWorkspace(), refItemUuid);
                    }
                }
            }

            if (StringUtils.isNotBlank(referencedPropertyValue)) {
                res.append(referencedPropertyValue);

            }

            if (StringUtils.isNotBlank(definition.getPropertyName())) {

                String itemPropertyValue = String.valueOf(item.getItemProperty(definition.getPropertyName()));
                if (StringUtils.isNotBlank(itemPropertyValue) && !"null".equals(itemPropertyValue)) {
                    res.append(" ").append(itemPropertyValue);
                }
                // fallback: try to render node name. Will work e.g. for mgnl:folder
                else {

                    if (item instanceof JcrNodeAdapter) {
                        String nodeName = null;
                        try {
                            nodeName = ((JcrNodeAdapter) item).getJcrItem().getName();
                        } catch (RepositoryException e) {
                            log.error("Failed eval a node name", e);
                        }
                        if (StringUtils.isNotBlank(nodeName)) {
                            res.append(" ").append(nodeName);
                        }
                    }
                }
            }


        } else {
            res.append("Cannot render this column label.");
            log.warn("Cannot render this column label.");
        }
        res.append("</span>");
        return res;
    }
}
