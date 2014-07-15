/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.forge.addon.deltaspike.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModules;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

/**
 * @author rafaelbenevides
 *
 */
@FacetConstraint({ DeltaSpikeFacet.class })
public class DeltaSpikeInstallModulesCommand extends AbstractDeltaSpikeCommand {

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.ui.command.UICommand#execute(org.jboss.forge.addon.ui.context.UIExecutionContext)
     */
    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        Project project = getSelectedProject(context);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);

        Iterable<DeltaSpikeModule> selectedModules = dsModules.getValue();
        Set<DeltaSpikeModule> modulesInstalled = new HashSet<DeltaSpikeModule>();
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
            }
        }

        return Results.success("DeltaSpike modules installed:" + modulesInstalled);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.projects.ui.AbstractProjectCommand#isEnabled(org.jboss.forge.addon.ui.context.UIContext)
     */
    @Override
    public boolean isEnabled(UIContext context) {
        return (super.isEnabled(context) && !context.getProvider().isGUI());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#getCommandName()
     */
    @Override
    public String getCommandName() {
        return "DeltaSpike: Install Modules";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#getCommandDescription()
     */
    @Override
    public String getCommandDescription() {
        return "Install DeltaSpike modules";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.forge.addon.deltaspike.commands.AbstractDeltaSpikeCommand#prepareModulesList(org.jboss.forge.addon.ui.input
     * .UISelectMany)
     */
    @Override
    public void prepareModulesList(DeltaSpikeFacet deltaSpikeFacet, UISelectMany<DeltaSpikeModule> dsModules) {
        List<DeltaSpikeModule> modulesToInstall = new ArrayList<DeltaSpikeModule>(Arrays.<DeltaSpikeModule> asList(DeltaSpikeModules.values()));
        modulesToInstall.removeAll(deltaSpikeFacet.getInstalledModules());

        dsModules.setValueChoices(modulesToInstall);
    }

}
