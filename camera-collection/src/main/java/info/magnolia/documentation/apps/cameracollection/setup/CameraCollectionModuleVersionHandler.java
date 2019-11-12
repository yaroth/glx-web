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
package info.magnolia.documentation.apps.cameracollection.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.BootstrapResourcesTask;
import info.magnolia.module.delta.ConditionalDelegateTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.TaskExecutionException;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A subclass of {@link DefaultModuleVersionHandler} overriding #getExtraInstallTasks to ensure during installation, that there is a<br/>
 * site with:<br/>
 * - i18n enabled with "en" and "de" locales.<br/>
 * <br/>
 * Works on  <i>magnolia-community-webapp</i> and <i>magnolia-community-demo-bundle</i> bundles.<br/>
 * Usage on multisite enabled bundles may require modification.
 */
public class CameraCollectionModuleVersionHandler extends DefaultModuleVersionHandler {

    private static final String TRAVELDEMO_SITE_PATH = "/modules/travel-demo/config/travel";
    private static final String SITE_ROOT_PATH = "/modules/site/config/site";

    private static final Logger log = LoggerFactory.getLogger(CameraCollectionModuleVersionHandler.class);

    /**
     * We use this method to install some bootstraps which must be installed conditionally.<br/>
     * We assume operating on CE-bundle, "magnolia-community-webapp" or "magnolia-community-demo-bundle"
     */
    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> extraInstallTasks = new ArrayList<>();
        extraInstallTasks.addAll(getSitePreparationTasks(installContext));
        return extraInstallTasks;
    }

    /**
     * Creates a task which will install the i18n node directly onto the node "/modules/site/config/site".<br/>
     * If travel-demo is installed, this node is not required (but already there on /modules/travel-demo/config/travel (which is the site).
     */
    private List<Task> getSitePreparationTasks(InstallContext installContext) {
        List<Task> siteTasks = new ArrayList<>();

        // i18n
        ConditionalDelegateTask enableI18n = new ConditionalDelegateTask(
                "Install i18n with locales enabled onto site if required.",
                "Installing i18n with locales enabled onto site if required.",
                new BootstrapResourcesTask() {
                    @Override
                    protected String[] getResourcesToBootstrap(InstallContext installContext) {
                        return new String[]{"/mgnl-bootstrap-conditionally-applied/config.modules.site.config.site.i18n.xml"};
                    }
                }
        ) {
            @Override
            protected boolean condition(InstallContext installContext) throws TaskExecutionException {
                Session session;
                try {
                    session = installContext.getJCRSession(RepositoryConstants.CONFIG);
                    return !session.nodeExists(TRAVELDEMO_SITE_PATH) && session.nodeExists(SITE_ROOT_PATH) && !session.getNode(SITE_ROOT_PATH).hasProperty("extends");
                } catch (RepositoryException e) {
                    String errorMessage = "Failed to install the i18n node onto the site at ";
                    installContext.error(errorMessage, e);
                }
                return false;
            }
        };
        siteTasks.add(enableI18n);

        return siteTasks;
    }


}