package org.jboss.forge.addon.deltaspike.commands;

import java.util.EnumSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

@FacetConstraint({ DeltaSpikeFacet.class })
public class DeltaSpikeAddModulesCommand extends AbstractProjectCommand {

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    @WithAttributes(label = "DeltaSpike modules")
    private UISelectMany<DeltaSpikeModule> dsModules;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(DeltaSpikeAddModulesCommand.class)
            .name("DeltaSpike: Setup Modules")
            .category(Categories.create("DeltaSpike"));
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        Project project = getSelectedProject(builder);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);

        Set<DeltaSpikeModule> allModules =  EnumSet.allOf(DeltaSpikeModule.class);
        allModules.removeAll(deltaSpikeFacet.getInstalledModules());
        
        dsModules.setValueChoices(allModules);
        
        builder
            .add(dsModules);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        Project project = getSelectedProject(context);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);

        // Modules Install
        Iterable<DeltaSpikeModule> selectedModules = dsModules.getValue();
        for (DeltaSpikeModule dsModule : selectedModules) {
            deltaSpikeFacet.install(dsModule);
        }

        return Results.success("DeltaSpike selected modules installed!");
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