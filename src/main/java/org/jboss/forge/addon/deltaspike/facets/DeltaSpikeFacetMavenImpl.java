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
package org.jboss.forge.addon.deltaspike.facets;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.deltaspike.DeltaSpikeModules;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.facets.FacetNotFoundException;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

/**
 * @author rafaelbenevides
 *
 */
@FacetConstraint({ MavenFacet.class })
public class DeltaSpikeFacetMavenImpl extends AbstractFacet<Project> implements DeltaSpikeFacet {

    @Inject
    private DependencyInstaller dependencyInstaller;

    private static final String DELTASPIKE_PROPERTY = "deltaspike.version";

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.facets.Facet#install()
     */
    @Override
    public boolean install() {
        // Default Version
        setDeltaSpikeVersion("1.0.0");
        // Core Install
        Dependency coreApi = DependencyBuilder
            .create("org.apache.deltaspike.core:deltaspike-core-api:${deltaspike.version}:compile");
        Dependency coreImpl = DependencyBuilder
            .create("org.apache.deltaspike.core:deltaspike-core-impl:${deltaspike.version}:runtime");

        installDependency(getFaceted(), coreApi);
        installDependency(getFaceted(), coreImpl);

        return isInstalled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.facets.Facet#isInstalled()
     */
    @Override
    public boolean isInstalled() {
        try {
            return getFaceted().getFacet(MavenFacet.class).getProperties().containsKey(DELTASPIKE_PROPERTY);
        } catch (FacetNotFoundException e) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#getDeltaSpikeVersion()
     */
    @Override
    public String getDeltaSpikeVersion() {
        return getFaceted().getFacet(MavenFacet.class).getProperties().get(DELTASPIKE_PROPERTY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#setDeltaSpikeVersion(java.lang.String)
     */
    @Override
    public DeltaSpikeFacet setDeltaSpikeVersion(String version) {
        MavenFacet mavenFacet = getFaceted().getFacet(MavenFacet.class);
        Model model = mavenFacet.getModel();
        model.getProperties().put(DELTASPIKE_PROPERTY, version);
        // force write of changes
        mavenFacet.setModel(model);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#install(org.jboss.forge.addon.deltaspike.DeltaSpikeModule)
     */
    @Override
    public DeltaSpikeFacet install(DeltaSpikeModule deltaSpikeModule) {
        for (Dependency dependency : deltaSpikeModule.getDependencies()) {
            installDependency(getFaceted(), dependency);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#getInstalledModules()
     */
    @Override
    public Set<DeltaSpikeModule> getInstalledModules() {
        Set<DeltaSpikeModule> installedModules = new HashSet<DeltaSpikeModule>();
        for (DeltaSpikeModules module : DeltaSpikeModules.values()) {
            if (isModuleInstalled(module)) {
                installedModules.add(module);
            }
        }
        return installedModules;
    }

    private void installDependency(Project project, Dependency dependency) {
        dependencyInstaller.installManaged(project, dependency);
        dependencyInstaller.install(project, dependency);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#isModuleInstalled(org.jboss.forge.addon.deltaspike.DeltaSpikeModule
     * )
     */
    @Override
    public boolean isModuleInstalled(DeltaSpikeModule deltaSpikeModule) {
        return dependencyInstaller.isInstalled(getFaceted(), deltaSpikeModule.getDependencies()[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet#remove(org.jboss.forge.addon.deltaspike.DeltaSpikeModule)
     */
    @Override
    public DeltaSpikeFacet remove(DeltaSpikeModule deltaSpikeModule) {
        DependencyFacet dependencyFacet = getFaceted().getFacet(DependencyFacet.class);
        for (Dependency dependency : deltaSpikeModule.getDependencies()) {
            dependencyFacet.removeManagedDependency(dependency);
            dependencyFacet.removeDependency(dependency);
        }
        return this;
    }
}
