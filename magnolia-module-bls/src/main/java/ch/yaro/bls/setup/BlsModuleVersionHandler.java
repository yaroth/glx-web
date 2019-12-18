package ch.yaro.bls.setup;

import info.magnolia.jcr.nodebuilder.task.NodeBuilderTask;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.nodebuilder.task.ErrorHandling;
import info.magnolia.repository.RepositoryConstants;

/**
 * This class is optional and lets you manage the versions of your module,
 * by registering "deltas" to maintain the module's configuration, or other type of content.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 *
 * @see info.magnolia.module.DefaultModuleVersionHandler
 * @see info.magnolia.module.ModuleVersionHandler
 * @see info.magnolia.module.delta.Task
 */
public class BlsModuleVersionHandler extends DefaultModuleVersionHandler {



}
