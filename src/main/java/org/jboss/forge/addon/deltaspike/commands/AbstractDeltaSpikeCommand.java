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

import javax.inject.Inject;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
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
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * @author rafaelbenevides
 *
 */
public abstract class AbstractDeltaSpikeCommand extends AbstractProjectCommand {

    @Inject
    @WithAttributes(label = "DeltaSpike modules", required = true, name = "modules")
    protected UISelectMany<DeltaSpikeModule> dsModules;

    @Inject
    private ProjectFactory projectFactory;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(DeltaSpikeManageModulesCommand.class)
            .name(getCommandName())
            .description(getCommandDescription())
            .category(Categories.create("DeltaSpike"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.ui.command.UICommand#initializeUI(org.jboss.forge.addon.ui.context.UIBuilder)
     */
    @Override
    public void initializeUI(UIBuilder builder) throws Exception {
        Project project = getSelectedProject(builder);
        DeltaSpikeFacet deltaSpikeFacet = project.getFacet(DeltaSpikeFacet.class);
        prepareModulesList(deltaSpikeFacet, dsModules);

        builder.add(dsModules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.ui.command.UICommand#execute(org.jboss.forge.addon.ui.context.UIExecutionContext)
     */
    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        // TODO Auto-generated method stub
        return null;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.projects.ui.AbstractProjectCommand#getProjectFactory()
     */
    @Override
    protected ProjectFactory getProjectFactory() {
        return projectFactory;
    }

    public abstract String getCommandName();

    public abstract String getCommandDescription();

    public abstract void prepareModulesList(DeltaSpikeFacet deltaSpikeFacet, UISelectMany<DeltaSpikeModule> dsModules);

}
