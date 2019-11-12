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
package info.magnolia.documentation.apps.cameracollection.pojos;

import info.magnolia.context.MgnlContext;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.documentation.apps.cameracollection.CameraCollectionModule;
import info.magnolia.jcr.predicate.AbstractPredicate;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.jcr.wrapper.I18nNodeWrapper;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.templating.functions.TemplatingFunctions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service class to create POJOs such as {@link Camera}, {@link Maker}, {@link Category} and {@link Image}
 * by given uuid or path to jcr nodes of the content apps 'cameras', 'makers' 'assets' and 'categories'.
 */
@Singleton
public class CameraCollectionPojoService {

    private static final Logger log = LoggerFactory.getLogger(CameraCollectionPojoService.class);

    private final DamTemplatingFunctions damfn;
    private final TemplatingFunctions cmsfn;

    /**
     * Node filter for "mgnl:camera".
     */
    private static AbstractPredicate<Node> CAMERA_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(CameraCollectionModule.CAMERA_NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };

    /**
     * Node filter for "mgnl:maker".
     */
    private static AbstractPredicate<Node> MAKER_FILTER = new AbstractPredicate<Node>() {
        @Override
        public boolean evaluateTyped(Node node) {
            try {
                String nodeTypeName = node.getPrimaryNodeType().getName();
                return nodeTypeName.equals(CameraCollectionModule.MAKER_NODETYPE);
            } catch (RepositoryException e) {
                log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
            }
            return false;
        }
    };


    @Inject
    public CameraCollectionPojoService(final DamTemplatingFunctions damfn, final TemplatingFunctions cmsfn) {
        this.damfn = damfn;
        this.cmsfn = cmsfn;
    }

    public List<Camera> searchCameras(String r) throws RepositoryException {
        if(StringUtils.isEmpty(r)){
            return new ArrayList<>();
        }

        r = r.toLowerCase();
        String newQuery = "select camera.name from [mgnl:camera] as camera INNER JOIN [mgnl:maker] AS maker ON camera.maker = maker.[jcr:uuid] where LOWER(maker.name) like '%"+r+"%' OR LOWER(camera.name) like '%"+r+"%'";

        //String queryStatement = "select * from [nt:base] where lower(name) like '%"+query.toLowerCase()+"%'";

        List<Camera> result = null;


        QueryManager qm =  MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE).getWorkspace().getQueryManager();
        Query query = qm.createQuery(newQuery, "JCR-SQL2");

        QueryResult queryResult = query.execute();

        RowIterator rowIterator = queryResult.getRows();


        if (rowIterator != null && rowIterator.hasNext()){
            result = new ArrayList<>();
            while (rowIterator.hasNext()){

                Row row = rowIterator.nextRow();

                Node node = row.getNode("camera");

                if(NodeUtil.isNodeType(node, CameraCollectionModule.CAMERA_NODETYPE)){
                    Camera camera = getCameraByNode(node);
                    result.add(camera);

                }
            }
        }
        return result;
    }


    /**
     * Returns a list of all {@link Maker} POJOs.
     */
    public List<Maker> getAllMakers() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node makersRootNode = session.getNode(CameraCollectionModule.MAKERS_BASEPATH);
        List<Maker> result = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(makersRootNode, MAKER_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Maker maker = getMakerByNode(node);
                if (maker != null) {
                    result.add(maker);
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of all {@link Maker} POJOs.
     */
    public List<Camera> getAllCameras() throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node camerasRootNode = session.getNode(CameraCollectionModule.CAMERAS_BASEPATH);
        List<Camera> result = new ArrayList<>();
        Iterable<Node> nodes = NodeUtil.collectAllChildren(camerasRootNode, CAMERA_FILTER);
        if (nodes != null) {
            for (Node node : nodes) {
                Camera camera = getCameraByNode(node);
                if (camera != null) {
                    result.add(camera);
                }
            }
        }
        return result;
    }


    /**
     * Returns a {@link Maker} POJO by a given uuid to a maker node.
     */
    public Maker getMakerById(String uuid) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node node = session.getNodeByIdentifier(uuid);
        if (node != null) {
            return getMakerByNode(node);
        }
        return null;
    }

    /**
     * Returns a {@link Maker} POJO by a given path to a maker node.
     */
    public Maker getMakerByPath(String path) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node node = session.getNode(path);
        if (node != null) {
            return getMakerByNode(node);
        }
        return null;
    }

    /**
     * Returns a {@link Maker} POJO by a given {@link Node}.
     */
    protected Maker getMakerByNode(Node node) throws RepositoryException {
        if (node == null || !CameraCollectionModule.MAKER_NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        node = i18nWrapNode(node);
        Maker maker = createBasicNodeItem(node, Maker.class);
        maker.setMakerPath(maker.getPath().substring(CameraCollectionModule.MAKERS_BASEPATH.length()));
        maker.setName(PropertyUtil.getString(node, "name", ""));
        maker.setLongName(PropertyUtil.getString(node, "longName", ""));
        maker.setHistory(PropertyUtil.getString(node, "history", ""));
        // single image
        String assetReferencePropertyValue = PropertyUtil.getString(node, "image");
        if (StringUtils.isNotBlank(assetReferencePropertyValue)) {
            if (StringUtils.isNotBlank(assetReferencePropertyValue)) {
                Image image = getImage(assetReferencePropertyValue);
                if (image != null) {
                    maker.setImage(image);
                }
            }
        }
        // categories
        maker.setCategories(getCategoriesList(node));

        return maker;
    }

    /**
     * Returns a {@link Camera} POJO by a given uuid to a camera node.
     */
    public Camera getCameraById(String uuid) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node node = session.getNodeByIdentifier(uuid);
        if (node != null) {
            return getCameraByNode(node);
        }
        return null;

    }

    /**
     * Returns a {@link Camera} POJO by a given path.
     */
    public Camera getCameraByPath(String path) throws RepositoryException {
        Session session = MgnlContext.getJCRSession(CameraCollectionModule.WORKSPACE);
        Node node = session.getNode(path);
        if (node != null) {
            return getCameraByNode(node);
        }
        return null;

    }

    /**
     * Returns a {@link Camera} POJO by a given {@link Node}.
     */
    protected Camera getCameraByNode(Node node) throws RepositoryException {
        if (node == null || !CameraCollectionModule.CAMERA_NODETYPE.equals(node.getPrimaryNodeType().getName())) {
            return null;
        }
        node = i18nWrapNode(node);
        Camera camera = createBasicNodeItem(node, Camera.class);
        camera.setCameraPath(camera.getPath().substring(CameraCollectionModule.CAMERAS_BASEPATH.length()));
        String cameraName = PropertyUtil.getString(node, "name", "");
        camera.setName(cameraName);
        camera.setDescription(PropertyUtil.getString(node, "description", ""));

        // maker and composed name
        String makerReferencePropertyValue = PropertyUtil.getString(node, "maker");
        if (StringUtils.isNotBlank(makerReferencePropertyValue)) {
            Maker maker = getMakerById(makerReferencePropertyValue);
            if(maker!=null){
                if (StringUtils.isNotBlank(maker.getName()) && StringUtils.isNotBlank(cameraName)) {
                    camera.setComposedName(maker.getName() + " " + cameraName);
                }
                camera.setMaker(maker);
            }
        }

        // categories
        camera.setCategories(getCategoriesList(node));

        // images
        List<Image> imageList = new ArrayList<>();
        List<String> assetKeys = getPropertyValuesList(node, "images");
        if (assetKeys.size() > 0) {
            for (String assetKey : assetKeys) {
                Image img = getImage(assetKey);
                if (img != null) {
                    imageList.add(img);
                }
            }

        }
        camera.setImages(imageList);

        return camera;
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

    /**
     * This methods wraps the node with {@link I18nNodeWrapper} - if still required
     * and encodes HTML to enable valid json.
     */
    private Node i18nWrapNode(Node node) {
        if (node != null) {
            if (!NodeUtil.isWrappedWith(node, I18nNodeWrapper.class)) {
                node = cmsfn.wrapForI18n(node);
            }
        }
        return node;
    }


}
