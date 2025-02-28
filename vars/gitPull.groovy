def call(String gitUrl, String gitBranch, String gitCredentials) {
    def steps = this.steps // Access Jenkins pipeline steps
    def env = this.env //Access Jenkins pipeline environment variables

    def branchName = gitBranch.replaceAll('^origin/', '')
    steps.checkout([$class: 'GitSCM',
                    branches: [[name: branchName]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: gitCredentials, url: gitUrl]]])
    steps.echo "${gitUrl}"
    steps.echo "${gitBranch}"
}
