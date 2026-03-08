package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

/**
 * Orchestrates the build pipeline: runs tests, deploys, and notifies.
 * Each phase is isolated in its own method for clarity and testability.
 */
public class Pipeline {

    private final Logger log;
    private final PipelineNotifier notifier;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.log = log;
        this.notifier = new PipelineNotifier(config, emailer, log);
    }

    public void run(Project project) {
        boolean testsPassed = runTestsPhase(project);
        boolean deploySuccessful = testsPassed && runDeploymentPhase(project);
        notifier.sendNotification(testsPassed, deploySuccessful);
    }

    private boolean runTestsPhase(Project project) {
        if (!project.hasTests()) {
            log.info("No tests");
            return true;
        }
        boolean passed = project.testsPass();
        if (passed) {
            log.info("Tests passed");
        } else {
            log.error("Tests failed");
        }
        return passed;
    }

    private boolean runDeploymentPhase(Project project) {
        boolean successful = project.deploysToProductionSuccessfully();
        if (successful) {
            log.info("Deployment successful");
        } else {
            log.error("Deployment failed");
        }
        return successful;
    }
}
