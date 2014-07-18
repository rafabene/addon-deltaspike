package org.jboss.forge.addon.deltaspike.extras;

import java.util.List;

import org.jboss.forge.addon.javaee.cdi.CDIFacet;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_0;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1;
import org.jboss.forge.addon.projects.Project;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;

public abstract class AbstractExtraSteps implements ModuleInstallationExtra {

    protected void installInterceptor(Project project, String interceptor) {
        CDIFacet<?> cdi = project.getFacet(CDIFacet.class);
        if (cdi instanceof CDIFacet_1_0)
        {
            CDIFacet_1_0 cdiFacet_1_0 = (CDIFacet_1_0) cdi;
            BeansDescriptor config = cdiFacet_1_0.getConfig();
            if (!config.getOrCreateInterceptors().getAllClazz().contains(interceptor)) {
                config.getOrCreateInterceptors().clazz(interceptor);
            }
            cdiFacet_1_0.saveConfig(config);
        }
        else if (cdi instanceof CDIFacet_1_1)
        {
            CDIFacet_1_1 cdiFacet_1_1 = (CDIFacet_1_1) cdi;
            org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor config = cdiFacet_1_1.getConfig();
            if (!config.getOrCreateInterceptors().getAllClazz().contains(interceptor)) {
                config.getOrCreateInterceptors().clazz(interceptor);
            }
            cdiFacet_1_1.saveConfig(config);
        }
        else {
            throw new IllegalStateException("Invalid CDI implementation. Only 1.0 and 1.1 supported");
        }
    }

    protected void removeInterceptor(Project project, String interceptor) {
        CDIFacet<?> cdi = project.getFacet(CDIFacet.class);
        if (cdi instanceof CDIFacet_1_0)
        {
            CDIFacet_1_0 cdiFacet_1_0 = (CDIFacet_1_0) cdi;
            BeansDescriptor config = cdiFacet_1_0.getConfig();
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
            cdiFacet_1_0.saveConfig(config);
        }
        else if (cdi instanceof CDIFacet_1_1)
        {
            CDIFacet_1_1 cdiFacet_1_1 = (CDIFacet_1_1) cdi;
            org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor config = cdiFacet_1_1.getConfig();
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
            cdiFacet_1_1.saveConfig(config);
        }
        else {
            throw new IllegalStateException("Invalid CDI implementation. Only 1.0 and 1.1 supported");
        }
    }

}
