package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;

/**
 * Responsible for sending notifications at the end of a pipeline run.
 * Isolates the email/notification concern from the pipeline orchestration logic.
 */
public class PipelineNotifier {

    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public PipelineNotifier(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void sendNotification(boolean testsPassed, boolean deploySuccessful) {
        if (!config.sendEmailSummary()) {
            log.info("Email disabled");
            return;
        }
        log.info("Sending email");
        emailer.send(buildEmailMessage(testsPassed, deploySuccessful));
    }

    private String buildEmailMessage(boolean testsPassed, boolean deploySuccessful) {
        if (!testsPassed) {
            return "Tests failed";
        }
        return deploySuccessful ? "Deployment completed successfully" : "Deployment failed";
    }
}
