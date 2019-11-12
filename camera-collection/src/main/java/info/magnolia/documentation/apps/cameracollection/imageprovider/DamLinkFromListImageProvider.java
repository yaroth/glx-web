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
package info.magnolia.documentation.apps.cameracollection.imageprovider;

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.AssetProviderRegistry;
import info.magnolia.dam.api.ItemKey;
import info.magnolia.dam.app.ui.imageprovider.DamLinkImageProvider;
import info.magnolia.dam.jcr.DamConstants;
import info.magnolia.dam.jcr.JcrAsset;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.jcr.util.SessionUtil;
import info.magnolia.link.LinkUtil;
import info.magnolia.ui.vaadin.integration.contentconnector.ContentConnector;

import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * DamLinkFromListImageProvider acts very similar as DamLinkImageProvider,
 * however, the content app item can have one image or a list - from which the image provider will grab the first.<br/>
 */
public class DamLinkFromListImageProvider extends DamLinkImageProvider {

    private static final Logger log = LoggerFactory.getLogger(DamLinkFromListImageProvider.class);

    private DamLinkFromListImageProviderDefinition definition;
    private AssetProviderRegistry assetProviderRegistry;

    @Inject
    public DamLinkFromListImageProvider(DamLinkFromListImageProviderDefinition definition, ContentConnector contentConnector, AssetProviderRegistry assetProviderRegistry) {
        super(definition, contentConnector, assetProviderRegistry);
        this.definition = definition;
        this.assetProviderRegistry = assetProviderRegistry;
    }


    @Override
    protected String getGeneratorImagePath(String workspace, Node node, String defaultGenerator) {
        String imagePath = null;

        if (node != null) {
            try {
                ItemKey itemKey = null;
                JcrAsset asset = null;
                String damLinkPropertyName = definition.getDamLinkPropertyName();

                if (node.hasProperty(damLinkPropertyName)) {

                    String damLink = null;
                    String damLinkType = definition.getDamLinkType();

                    if (damLinkType.equals(LinkPropertyType.single.toString())) {
                        damLink = node.getProperty(damLinkPropertyName).getString();
                    } else if (damLinkType.equals(LinkPropertyType.listInSingleProperty.toString())) {
                        Object property = PropertyUtil.getPropertyValueObject(node, definition.getDamLinkPropertyName());
                        if (property instanceof List) {
                            try {
                                damLink = (String) ((List) property).get(0);
                            } catch (Exception e) {
                                log.error("Failed to get a value from a multi value property.", e);
                            }
                        }
                    } else {
                        log.warn("Invalid damLinkType! Please check your defintion for {}", definition.getClass().getName());
                    }

                    if (StringUtils.isNotBlank(damLink)) {
                        itemKey = ItemKey.from(damLink);
                        asset = (JcrAsset) assetProviderRegistry.getProviderById(itemKey.getProviderId()).getAsset(itemKey);
                        imagePath = asset.getLink();
                    }
                }

                if (asset != null) {
                    Node assetNode = SessionUtil.getNodeByIdentifier(DamConstants.WORKSPACE, itemKey.getAssetId());
                    Node imageNode = assetNode.getNode(JcrConstants.JCR_CONTENT);
                    imagePath = MgnlContext.getContextPath() + "/" + definition.getImagingServletPath() + "/" + defaultGenerator + "/" + asset.getAssetProvider().getWorkspaceName() + "/" + imageNode.getIdentifier() + "/" + asset.getFileName();
                }

                // Add cache fingerprint so that browser caches asset only until asset is modified.
                Calendar lastModified = NodeTypes.LastModified.getLastModified(node);
                imagePath = LinkUtil.addFingerprintToLink(imagePath, lastModified);

            } catch (RepositoryException e) {
                log.warn("Could not get name or identifier from imageNode: {}", e.getMessage());
            }
        }

        return imagePath;
    }

}
