package org.jboss.forge.addon.deltaspike.commands;

import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyQuery;
import org.jboss.forge.addon.dependencies.DependencyRepository;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.dependencies.util.NonSnapshotDependencyFilter;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.furnace.util.Predicate;

public class DeltaSpikeSetupCommand extends AbstractProjectCommand {

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    private DependencyResolver dependencyResolver;

    @Inject
    private DependencyInstaller dependencyInstaller;

    @Inject
    private UISelectOne<Coordinate> dsVersions;

    @Inject
    private UISelectMany<Dependency> dsModules;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(DeltaSpikeSetupCommand.class)
            .name("DeltaSpike: setup")
            .category(Categories.create("DeltaSpike"));
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        DependencyQuery dependencyQuery = DependencyQueryBuilder
            .create("org.apache.deltaspike.core:deltaspike-core-api")
            .setFilter(new NonSnapshotDependencyFilter());
        List<Coordinate> versions = dependencyResolver.resolveVersions(dependencyQuery);
        dsVersions.setValueChoices(versions);
        dsVersions.setLabel("DeltaSpike version");
        dsModules.setLabel("DeltaSpike Modules");
        builder
            .add(dsVersions)
            .add(dsModules);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {

        return Results.fail("Not implemented!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.projects.ui.AbstractProjectCommand#getProjectFactory()
     */
    @Override
    protected ProjectFactory getProjectFactory() {
        return projectFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.projects.ui.AbstractProjectCommand#isProjectRequired()
     */
    @Override
    protected boolean isProjectRequired() {
        return true;
    }
}