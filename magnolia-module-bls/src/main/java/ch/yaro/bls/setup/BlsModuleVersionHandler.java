package ch.yaro.bls.setup;

import info.magnolia.jcr.nodebuilder.task.NodeBuilderTask;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.*;
import info.magnolia.nodebuilder.task.ErrorHandling;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.setup.initial.GenericTasks;

import java.util.ArrayList;
import java.util.List;

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


    public BlsModuleVersionHandler() {
        super();
    }



//    @Override
//    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
//
//        ArrayList<Task> tasks = new ArrayList<>();
//        tasks.addAll(super.getExtraInstallTasks(installContext));
//
//        tasks.add(new SetPropertyTask("Disable publishing to magnoliaPublic8080", RepositoryConstants.CONFIG, "/modules/publishing-core/config/receivers/magnoliaPublic8080", "enabled", "false"));
//
//        return tasks;
//    }

    @Override
    protected List<Task> getBasicInstallTasks(InstallContext ctx) {
        final List<Task> tasks = new ArrayList<>(super.getBasicInstallTasks(ctx));
        tasks.add(new SetPropertyTask("Disable publishing to magnoliaPublic8080", RepositoryConstants.CONFIG, "/modules/publishing-core/config/receivers/magnoliaPublic8080", "enabled", "false"));

        return tasks;
    }




}
