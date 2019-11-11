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

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.jcr.wrapper.I18nNodeWrapper;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.templating.functions.TemplatingFunctions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class BlsPojoService {

    private static final Logger log = LoggerFactory.getLogger(BlsPojoService.class);

    private final DamTemplatingFunctions damfn;
    private final TemplatingFunctions cmsfn;


    @Inject
    public BlsPojoService(final DamTemplatingFunctions damfn, final TemplatingFunctions cmsfn) {
        this.damfn = damfn;
        this.cmsfn = cmsfn;
    }



    /**
     * Returns an {@link Image} POJO by a given assetKey (as String).
     */
    public Image getImage(String assetKey) {
        String assetLink = damfn.getAssetLink(assetKey);
        if (StringUtils.isNotBlank(assetLink)) {
            Image image = new Image();
            image.setAssetKey(assetKey);
            image.setLink(assetLink);
            return image;
        }
        return null;
    }

    /**
     * Returns a {@link Category} POJO by a given uuid.
     */
    public Category getCategory(String uuid) throws RepositoryException {
        Category category = null;
        if (StringUtils.isNotBlank(uuid)) {
            Session session = MgnlContext.getJCRSession(CategorizationModule.CATEGORIZATION_WORKSPACE);
            Node node = session.getNodeByIdentifier(uuid);
            if (node != null) {
                category = createBasicNodeItem(node, Category.class);
                category.setName(PropertyUtil.getString(node, "name", ""));
            }
        }
        return category;
    }

    /**
     * The methods instantiates and returns an object by the given type and sets the base properties of the base class {@link NodeItem}.
     */
    protected <T extends NodeItem> T createBasicNodeItem(Node node, Class<T> clazz) throws RepositoryException {
        T nodeItem = null;
        try {
            Class classDefinition = Class.forName(clazz.getName());
            nodeItem = (T) classDefinition.newInstance();
            nodeItem.setNodeName(node.getName());
            nodeItem.setUuid(node.getIdentifier());
            nodeItem.setPath(node.getPath());
            nodeItem.setWorkspace(node.getSession().getWorkspace().getName());

        } catch (InstantiationException e) {
            log.error("NodeItem instantiation failed due to InstantiationException.", e);
        } catch (IllegalAccessException e) {
            log.error("NodeItem instantiation failed due to illegal access.", e);
        } catch (ClassNotFoundException e) {
            log.error("NodeItem instantiation failed due to class not found.", e);
        }
        return nodeItem;
    }


    /**
     * Add the bean property "categories" to the given nodeItem.
     */
    private List<Category> getCategoriesList(Node node) throws RepositoryException {
        List<Category> categoryList = new ArrayList<>();
        List<String> categoryUuids = getPropertyValuesList(node, "categories");
        if (categoryUuids.size() > 0) {

            for (String catUuid : categoryUuids) {
                Category cat = getCategory(catUuid);
                if (cat != null) {
                    categoryList.add(cat);
                }
            }
        }
        return categoryList;
    }

    /**
     * This methods returns a list of Strings containing the values of a multi value property.<br/>
     * The method assumes the values are strings. If a value is not a STring, it is not added to the returned list.
     */
    private List<String> getPropertyValuesList(Node node, String propertyName) throws RepositoryException {
        List<String> result = new ArrayList<>();
        if (node.hasProperty(propertyName)) {
            Property property = node.getProperty(propertyName);
            if (property != null) {
                if (property.getValues() != null) {
                    for (Value value : property.getValues()) {
                        try {
                            String propertyValue = value.getString();
                            result.add(propertyValue);
                        } catch (Exception e) {
                            log.warn("Failed to get string from value from value-property-list");
                        }
                    }
                }
            }
        }
        return result;
    }



}
