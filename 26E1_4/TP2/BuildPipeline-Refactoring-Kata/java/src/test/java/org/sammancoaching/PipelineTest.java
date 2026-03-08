package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Project;
import org.sammancoaching.dependencies.TestStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PipelineTest {

    private Config config;
    private Emailer emailer;
    private CapturingLogger log;
    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        config = mock(Config.class);
        emailer = mock(Emailer.class);
        log = new CapturingLogger();
        pipeline = new Pipeline(config, emailer, log);
    }

    // --- Tests pass + Deploy success ---

    @Test
    void project_with_passing_tests_and_successful_deploy_sends_success_email() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void project_with_passing_tests_and_successful_deploy_logs_all_phases() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        assertThat(log.getLoggedLines()).contains(
                "INFO: Tests passed",
                "INFO: Deployment successful",
                "INFO: Sending email"
        );
    }

    @Test
    void project_with_passing_tests_and_successful_deploy_does_not_send_email_when_disabled() {
        when(config.sendEmailSummary()).thenReturn(false);
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        verifyNoInteractions(emailer);
        assertThat(log.getLoggedLines()).contains("INFO: Email disabled");
    }

    // --- Tests pass + Deploy fails ---

    @Test
    void project_with_passing_tests_and_failed_deploy_sends_deployment_failure_email() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(false)
                .build();

        pipeline.run(project);

        verify(emailer).send("Deployment failed");
        assertThat(log.getLoggedLines()).contains("ERROR: Deployment failed");
    }

    @Test
    void project_with_passing_tests_and_failed_deploy_does_not_send_email_when_disabled() {
        when(config.sendEmailSummary()).thenReturn(false);
        Project project = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(false)
                .build();

        pipeline.run(project);

        verifyNoInteractions(emailer);
        assertThat(log.getLoggedLines()).contains("INFO: Email disabled");
    }

    // --- Tests fail ---

    @Test
    void project_with_failing_tests_does_not_attempt_deployment() {
        when(config.sendEmailSummary()).thenReturn(false);
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        assertThat(log.getLoggedLines()).contains("ERROR: Tests failed");
        assertThat(log.getLoggedLines()).noneMatch(line -> line.contains("Deployment"));
    }

    @Test
    void project_with_failing_tests_sends_test_failure_email() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        verify(emailer).send("Tests failed");
    }

    @Test
    void project_with_failing_tests_does_not_send_email_when_disabled() {
        when(config.sendEmailSummary()).thenReturn(false);
        Project project = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        verifyNoInteractions(emailer);
        assertThat(log.getLoggedLines()).contains("INFO: Email disabled");
    }

    // --- No tests ---

    @Test
    void project_without_tests_and_successful_deploy_sends_success_email() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        pipeline.run(project);

        verify(emailer).send("Deployment completed successfully");
        assertThat(log.getLoggedLines()).contains("INFO: No tests");
    }

    @Test
    void project_without_tests_and_failed_deploy_sends_failure_email() {
        when(config.sendEmailSummary()).thenReturn(true);
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(false)
                .build();

        pipeline.run(project);

        verify(emailer).send("Deployment failed");
    }

    @Test
    void project_without_tests_and_failed_deploy_does_not_send_email_when_disabled() {
        when(config.sendEmailSummary()).thenReturn(false);
        Project project = Project.builder()
                .setTestStatus(TestStatus.NO_TESTS)
                .setDeploysSuccessfully(false)
                .build();

        pipeline.run(project);

        verifyNoInteractions(emailer);
        assertThat(log.getLoggedLines()).contains("INFO: Email disabled");
    }
}

