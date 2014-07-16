package org.jboss.forge.addon.deltaspike.extras;

import java.util.List;

import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_0;
import org.jboss.forge.addon.projects.Project;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;

public abstract class AbstractExtraSteps implements ModuleInstallationExtra {

    protected void installInterceptor(Project project, String interceptor) {
        CDIFacet_1_0 cdiFacet = project.getFacet(CDIFacet_1_0.class);
        BeansDescriptor config = cdiFacet.getConfig();
        if (!config.getOrCreateInterceptors().getAllClazz().contains(interceptor)) {
            config.getOrCreateInterceptors().clazz(interceptor);
        }

        config.getOrCreateInterceptors().clazz();
        cdiFacet.saveConfig(config);
    }

    protected void removeInterceptor(Project project, String interceptor) {
        CDIFacet_1_0 cdiFacet = project.getFacet(CDIFacet_1_0.class);
        BeansDescriptor config = cdiFacet.getConfig();
        // Get all Interceptors
        List<String> allClasses = config.getOrCreateInterceptors().getAllClazz();
        // Remote the interceptor
        allClasses.remove(interceptor);
        // Remove all Interptors since there's no remove a single interceptor implementation
        config.getOrCreateInterceptors().removeAllClazz();
        // Install all left Interceptors
        for (String clazz : allClasses) {
            config.getOrCreateInterceptors().clazz(clazz);
        }
        cdiFacet.saveConfig(config);
    }

}
