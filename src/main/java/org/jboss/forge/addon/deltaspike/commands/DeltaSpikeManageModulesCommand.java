package org.jboss.forge.addon.deltaspike.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModules;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_0;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

@FacetConstraint({ DeltaSpikeFacet.class, CDIFacet_1_0.class })
public class DeltaSpikeManageModulesCommand extends AbstractDeltaSpikeCommand {

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
                if (selectedModule.equals(dsModule)) {
                    selected = true;
                }
            }
            // Modules to Install
            if (selected && !deltaSpikeFacet.isModuleInstalled(dsModule)) {
                modulesInstalled.add(dsModule);
                deltaSpikeFacet.install(dsModule);
                if (dsModule.getInstallationExtraStep() != null){
                    dsModule.getInstallationExtraStep().install(project);
                }
            }
            // Modules to Remove
            if (!selected && deltaSpikeFacet.isModuleInstalled(dsModule)) {
                modulesRemoved.add(dsModule);
                deltaSpikeFacet.remove(dsModule);
                if (dsModule.getInstallationExtraStep() != null){
                    dsModule.getInstallationExtraStep().remove(project);
                }
            }
        }

        return Results.success("DeltaSpike modules installed:" + modulesInstalled + "\nDeltaSpike modules removed:" + modulesRemoved);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.projects.ui.AbstractProjectCommand#isEnabled(org.jboss.forge.addon.ui.context.UIContext)
     */
    @Override
    public boolean isEnabled(UIContext context) {
        return (super.isEnabled(context) && context.getProvider().isGUI());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#getCommandName()
     */
    @Override
    public String getCommandName() {
        return "DeltaSpike: Manage Modules";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#getCommandDescription()
     */
    @Override
    public String getCommandDescription() {
        return "Managed DeltaSpike modules - Only in GUI mode";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#prepareModulesList(org.jboss.forge.addon.deltaspike
     * .facets.DeltaSpikeFacet, org.jboss.forge.addon.ui.input.UISelectMany)
     */
    @Override
    public void prepareModulesList(DeltaSpikeFacet deltaSpikeFacet, UISelectMany<DeltaSpikeModule> dsModules) {
        dsModules.setRequired(false);
        dsModules.setValueChoices(Arrays.<DeltaSpikeModule> asList(DeltaSpikeModules.values()));
        dsModules.setValue(deltaSpikeFacet.getInstalledModules());
    }

}