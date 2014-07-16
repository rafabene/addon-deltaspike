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
package org.jboss.forge.addon.deltaspike;

import static org.jboss.forge.addon.dependencies.builder.DependencyBuilder.create;

import org.jboss.forge.addon.deltaspike.extras.JPAExtraSteps;
import org.jboss.forge.addon.deltaspike.extras.ModuleInstallationExtra;
import org.jboss.forge.addon.deltaspike.extras.SecurityExtraSteps;
import org.jboss.forge.addon.dependencies.Dependency;

/**
 * @author rafaelbenevides
 *
 */
public enum DeltaSpikeModules implements DeltaSpikeModule {

    SECURITY("Security", new SecurityExtraSteps(),
        create("org.apache.deltaspike.modules:deltaspike-security-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-security-module-impl:${deltaspike.version}:runtime")),
    JPA("JPA", new JPAExtraSteps(),
        create("org.apache.deltaspike.modules:deltaspike-jpa-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-jpa-module-impl:${deltaspike.version}:runtime")),
    JSF("JSF",
        create("org.apache.deltaspike.modules:deltaspike-jsf-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-jsf-module-impl:${deltaspike.version}:runtime")),
    BEANVALIDATION("Bean Validation",
        create("org.apache.deltaspike.modules:deltaspike-bean-validation-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-bean-validation-module-impl:${deltaspike.version}:runtime")),
    SERVLET("Servlet",
        create("org.apache.deltaspike.modules:deltaspike-servlet-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-servlet-module-impl:${deltaspike.version}:runtime")),
    DATA("Data",
        create("org.apache.deltaspike.modules:deltaspike-data-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-data-module-impl:${deltaspike.version}:runtime")),
    TEST_CONTROL("Test-Control",
        create("org.apache.deltaspike.modules:deltaspike-test-control-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-test-control-module-impl:${deltaspike.version}:runtime")),
    SCHEDULER("Scheduler",
        create("org.apache.deltaspike.modules:deltaspike-scheduler-module-api:${deltaspike.version}:compile"),
        create("org.apache.deltaspike.modules:deltaspike-scheduler-module-impl:${deltaspike.version}:runtime"),
        create("org.quartz-scheduler:quartz:2.2.1:compile"));

    private DeltaSpikeModules(String name, ModuleInstallationExtra extra, Dependency... dependencies) {
        this.name = name;
        this.dependencies = dependencies;
        this.extraInstallationStep = extra;
    }

    private DeltaSpikeModules(String name, Dependency... dependencies) {
        this(name, null, dependencies);
    }

    private String name;
    private Dependency[] dependencies;
    private ModuleInstallationExtra extraInstallationStep;

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.DeltaSpikeModule#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.forge.addon.deltaspike.DeltaSpikeModule#getDependencies()
     */
    @Override
    public Dependency[] getDependencies() {
        return dependencies;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public ModuleInstallationExtra getInstallationExtraStep() {
        return extraInstallationStep;
    }

}
