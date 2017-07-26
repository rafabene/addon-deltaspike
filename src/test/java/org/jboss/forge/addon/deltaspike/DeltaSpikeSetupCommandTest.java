package org.jboss.forge.addon.deltaspike;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.deltaspike.commands.DeltaSpikeSetupCommand;
import org.jboss.forge.addon.deltaspike.facets.DeltaSpikeFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.test.UITestHarness;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DeltaSpikeSetupCommandTest
{

    @Inject
    private UITestHarness uiTestHarness;

    @Inject
    private ProjectFactory projectFactory;

    @Deployment
    @AddonDependencies
    public static AddonArchive getDeployment()
    {
    	AddonArchive archive = ShrinkWrap
            .create(AddonArchive.class)
            .addBeansXML()
            .addAsAddonDependencies(
                AddonDependencyEntry
                    .create("org.jboss.forge.addon:ui-test-harness"),
                AddonDependencyEntry
                    .create("org.jboss.forge.addon:projects"),
                AddonDependencyEntry
                    .create("org.jboss.forge.addon:maven"),
                AddonDependencyEntry
                    .create("org.jboss.forge.furnace.container:cdi"),
                AddonDependencyEntry
                    .create("org.jboss.forge.addon.deltaspike:addon-deltaspike"));
        return archive;
    }

    @Test
    @Ignore
    public void testSetup() throws Exception {
        Project project = projectFactory.createTempProject();
        try (CommandController controller = uiTestHarness.createCommandController(DeltaSpikeSetupCommand.class, project.getRoot());) {
            controller.initialize();
            Assert.assertTrue(controller.isValid());
            Result result = controller.execute();
            Assert.assertEquals("DeltaSpike core and selected modules installed!",result.getMessage());
        }
        project = projectFactory.findProject(project.getRoot());
        Assert.assertTrue(project.hasFacet(DeltaSpikeFacet.class));
    }
}