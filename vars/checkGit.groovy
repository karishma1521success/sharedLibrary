def call(String githubURL, String gitCredentials) { //Added optional String type hints.
    echo 'started the precondition check for check that it has right github credentials'
    def gitURL = githubURL.replace('https://', '')
    echo 'Checking GitHub repository availability...'
    // Use git ls-remote with credentials from Jenkins credentials
    this.steps.withCredentials([this.steps.usernamePassword(credentialsId: gitCredentials, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        def encodedUsername = java.net.URLEncoder.encode(USERNAME, 'UTF-8')
        def encodedPassword = java.net.URLEncoder.encode(PASSWORD, 'UTF-8')
        sh """
            set +x  # Disable echoing to hide sensitive information
            git ls-remote --exit-code https://${encodedUsername}:${encodedPassword}@${gitURL} || (echo 'GitHub repository is not reachable, exiting.' && exit 1)
            set -x  # Enable echoing
        """
    }
    echo 'GitHub repository is reachable. Proceeding with the pipeline.'
}
