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


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.documentation.apps.cameracollection.CameraCollectionModule;
import info.magnolia.templating.functions.TemplatingFunctions;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.junit.Before;
import org.junit.Test;

/**
 * A class to test {@link CameraCollectionPojoService}.*
 */
public class CameraCollectionPojoServiceTest {


    private static final String MOCKED_UUID = "lalala-bababababab-123";
    private static final String MOCKED_NodePath = "/cameras/F3";
    private static final String MOCKED_NodeName = "F3";
    private static final String MOCKED_Asset_itemKey = "jcr:1c7d863c-5a78-42c9-9182-ba2b2829f029";
    private static final String MOCKED_Asset_link = "http://localhost:8080/magnoliaAuthir/dam/blabla-lalal/tralala";


    private DamTemplatingFunctions damTemplatingFunctions;
    private CameraCollectionPojoService cameraCollectionPojoService;
    private TemplatingFunctions templatingFunctions;
    private Session session;

    @Before
    public void setUp() {
        damTemplatingFunctions = mock(DamTemplatingFunctions.class);
        templatingFunctions = mock(TemplatingFunctions.class);
        when(damTemplatingFunctions.getAssetLink(MOCKED_Asset_itemKey)).thenReturn(MOCKED_Asset_link);
        cameraCollectionPojoService = new CameraCollectionPojoService(damTemplatingFunctions, templatingFunctions);

        session = mock(Session.class);
        Workspace workspace = mock(Workspace.class);
        when(workspace.getName()).thenReturn(CameraCollectionModule.WORKSPACE);
        when(session.getWorkspace()).thenReturn(workspace);
    }

    /**
     * Test whether the generic typed method #createBasicNodeItem works fine.
     *
     * @throws Exception
     */
    @Test
    public void createBasicNodeItem() throws Exception {
        // GIVEN
        // WHEN
        Class clazz = Camera.class;
        NodeItem nodeItem = cameraCollectionPojoService.createBasicNodeItem(mockNode(), clazz);

        // THEN
        assertThat(nodeItem, allOf(
                instanceOf(Camera.class),
                not(nullValue())
        ));
    }

    @Test
    public void getImage() {
        // GIVEN
        // WHEN
        Image image = cameraCollectionPojoService.getImage(MOCKED_Asset_itemKey);

        // THEN
        assertThat(image, notNullValue());
        assertEquals(MOCKED_Asset_link, image.getLink());
    }

    private Node mockNode() throws Exception {
        Node node = mock(Node.class);
        when(node.getIdentifier()).thenReturn(MOCKED_UUID);
        when(node.getName()).thenReturn(MOCKED_NodeName);
        when(node.getPath()).thenReturn(MOCKED_NodePath);
        when(node.getSession()).thenReturn(session);
        return node;
    }


}
