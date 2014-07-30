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

import java.util.Set;

import org.jboss.forge.addon.deltaspike.DeltaSpikeModule;
import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * @author rafaelbenevides
 *
 */
public interface DeltaSpikeFacet extends ProjectFacet {
    
    String getDeltaSpikeVersion();
    
    DeltaSpikeFacet setDeltaSpikeVersion(String version);
    
    Set<DeltaSpikeModule> getInstalledModules();
    
    DeltaSpikeFacet install(DeltaSpikeModule deltaSpikeModule);
    
    DeltaSpikeFacet remove(DeltaSpikeModule deltaSpikeModule);
    
    boolean isModuleInstalled(DeltaSpikeModule deltaSpikeModule);

}
