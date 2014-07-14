package org.jboss.forge.addon.deltaspike.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModules;
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
public class DeltaSpikeManageModulesCommand extends AbstractProjectCommand {

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    @WithAttributes(label = "DeltaSpike modules")
    private UISelectMany<DeltaSpikeModule> dsModules;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(DeltaSpikeManageModulesCommand.class)
            .name("DeltaSpike: Manage Modules")
            .category(Categories.create("DeltaSpike"));
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        Project project = getSelectedProject(builder);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);

        dsModules.setValueChoices(Arrays.<DeltaSpikeModule> asList(DeltaSpikeModules.values()));
        dsModules.setValue(deltaSpikeFacet.getInstalledModules());

        builder.add(dsModules);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        Project project = getSelectedProject(context);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);


        Iterable<DeltaSpikeModule> selectedModules = dsModules.getValue();
        Set<DeltaSpikeModule> modulesInstalled = new HashSet<DeltaSpikeModule>();
        Set<DeltaSpikeModule> modulesRemoved = new HashSet<DeltaSpikeModule>();
        for (DeltaSpikeModule dsModule : DeltaSpikeModules.values()) {
            boolean selected = false;
            for (DeltaSpikeModule selectedModule : selectedModules) {
                if (selectedModule.getName().equals(dsModule.getName())) {
                    selected = true;
                }
            }
            // Modules to Install
            if (selected && !deltaSpikeFacet.isModuleInstalled(dsModule)) {
                modulesInstalled.add(dsModule);
                deltaSpikeFacet.install(dsModule);
            }
            // Modules to Remove
            if (!selected && deltaSpikeFacet.isModuleInstalled(dsModule)) {
                modulesRemoved.add(dsModule);
                deltaSpikeFacet.remove(dsModule);
            }
        }

        return Results.success("DeltaSpike modules installed:" + modulesInstalled + "\nDeltaSpike modules removed:" + modulesRemoved);
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