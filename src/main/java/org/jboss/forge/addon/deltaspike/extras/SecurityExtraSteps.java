package org.jboss.forge.addon.deltaspike.extras;

import org.jboss.forge.addon.projects.Project;

public class SecurityExtraSteps extends AbstractExtraSteps {

    private static final String INTERCEPTOR = "org.apache.deltaspike.security.impl.extension.SecurityInterceptor";

    @Override
    public void install(Project project) {
        installInterceptor(project, INTERCEPTOR);
    }

    @Override
    public void remove(Project project) {
        removeInterceptor(project, INTERCEPTOR);
    }

}
