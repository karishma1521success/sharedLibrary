def call(String buildResult) {
    def steps = this.steps 
    def env = this.env              //Fetching the jenkins pipeline environment variables.

    if (buildResult == 'FAILURE') {
        sendEmail(env.FAIL_SUBJECT, env.FAIL_CONTENT)
        steps.echo "Build result has changed to FAILURE"
    } else if (buildResult == 'UNSTABLE') {
        sendEmail(env.UNSTABLE_SUBJECT, env.UNSTABLE_CONTENT)
        steps.echo "Build result has changed to UNSTABLE"
    } else if (buildResult == 'SUCCESS') {
        sendEmail(env.PASS_SUBJECT, env.PASS_CONTENT)
        enableDisableJobs()
        steps.echo "Build SUCCESS"
    } else if (buildResult == 'ABORTED') {
        sendEmail("Build ${steps.BUILD_NUMBER} Aborted", "The build was aborted.")
        steps.echo "Build result has changed to ABORTED"
    } else {
        sendEmail("Unknown Build ${steps.BUILD_NUMBER}", "Unknown Build ${steps.BUILD_NUMBER}")
        steps.echo "Build result has changed to UNKNOWN"
    }
}

def sendEmail(String subject, String body) {
    steps.emailext subject: subject,
        body: body,
        attachLog: true,
        to: "${env.RECIPIENTS_NAME}"
}

def enableDisableJobs() {
    steps.withCredentials([steps.usernamePassword(credentialsId: 'CI_CD_Pipeline_Token_Gouravsingh', usernameVariable: 'master_usr', passwordVariable: 'master_passwd')]) {
        steps.sh """
            echo "Enabling ${env.CD_Pipeline_Name}"
            curl --fail -X POST "http://127.0.0.1:9090/job/MyMavenProject-CD/enable" --user ${master_usr}:${master_passwd} || echo "Failed to enable ${env.CD_Pipeline_Name}"
            echo "Disabling ${env.JOB_NAME}"
            curl --fail -X POST "http://127.0.0.1:9090/job/MyMavenProject-CI/disable" --user ${master_usr}:${master_passwd} || echo "Failed to disable ${env.JOB_NAME}"
        """
    }
}
