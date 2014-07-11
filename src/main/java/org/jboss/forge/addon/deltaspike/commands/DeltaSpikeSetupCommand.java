package org.jboss.forge.addon.deltaspike.commands;

import java.util.List;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.jboss.forge.addon.deltaspike.CoordinateVersionConverter;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.DependencyQuery;
import org.jboss.forge.addon.dependencies.DependencyRepository;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.dependencies.util.NonSnapshotDependencyFilter;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class DeltaSpikeSetupCommand extends AbstractProjectCommand {

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    private FacetFactory facetFactory;

    @Inject
    private DependencyResolver dependencyResolver;

    @Inject
    @WithAttributes(label = "DeltaSpike version", required = true)
    private UISelectOne<Coordinate> dsVersions;

    @Inject
    @WithAttributes(label = "DeltaSpike modules")
    private UISelectMany<DeltaSpikeModule> dsModules;

    private DependencyRepository mavenCentral = new DependencyRepository("maven-cetral", "http://central.maven.org/maven2");

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(DeltaSpikeSetupCommand.class)
            .name("DeltaSpike: setup")
            .category(Categories.create("DeltaSpike"));
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        // DeltaSpike Versions
        DependencyQuery dependencyQuery = DependencyQueryBuilder
            .create("org.apache.deltaspike.core:deltaspike-core-api")
            .setFilter(new NonSnapshotDependencyFilter())
            .setRepositories(mavenCentral);
        List<Coordinate> versions = dependencyResolver.resolveVersions(dependencyQuery);

        dsVersions.setValueChoices(versions);
        dsVersions.setItemLabelConverter(CoordinateVersionConverter.INSTANCE);
        dsVersions.setDefaultValue(versions.get(versions.size() - 1));

        builder
            .add(dsVersions)
            .add(dsModules);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        Coordinate dsVersionToInstall = dsVersions.getValue();
        Project project = getSelectedProject(context);
        DeltaSpikeFacet deltaSpikeFacet = facetFactory.install(project, DeltaSpikeFacet.class);
        deltaSpikeFacet.setDeltaSpikeVersion(dsVersionToInstall.getVersion());

        // Modules Install
        Iterable<DeltaSpikeModule> selectedModules = dsModules.getValue();
        for (DeltaSpikeModule dsModule : selectedModules) {
            deltaSpikeFacet.install(dsModule);
        }

        return Results.success("DeltaSpike core and selected modules installed!");
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