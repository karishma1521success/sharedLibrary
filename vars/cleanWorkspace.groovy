def call(boolean bypassPause) {
    def steps = this.steps // Access Jenkins pipeline steps
    def params = this.params // Access Jenkins pipeline parameters
    def env = this.env // Access Jenkins pipeline environment variables

    if (!bypassPause) {
        steps.echo 'Pausing the pipeline...'
        steps.input message: '''Clean Directory
        Do you want to continue?''', ok: 'Resume'
        steps.echo 'Resuming the pipeline...'
    } else {
        steps.echo 'Bypassing manual approval.'
    }

    steps.sh "rm -rf ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/*" // Use env for variables
    steps.echo 'Workspace Cleaned'
}
