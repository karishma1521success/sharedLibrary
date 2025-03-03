def call(String useSonar, String projectType, String sonarProjectKey, String sonarHostUrl, String sonarToken) {
    def steps = this.steps
    def env = this.env

    if (useSonar == 'true') {
        if (projectType == 'Gradle') {
            steps.echo 'Running SonarQube analysis (Gradle)...'
            steps.withEnv(["GRADLE_OPTS=-Xmx4096m -Dorg.gradle.daemon=true -Dorg.gradle.jvmargs='-Xmx4096m'"]) {
                steps.sh """
                    cd ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}
                    chmod +x gradlew

                    ./gradlew sonar \\
                    -Dsonar.projectKey=${sonarProjectKey} \\
                    -Dsonar.host.url=${sonarHostUrl} \\
                    -Dsonar.token=${sonarToken}
                """
            }
        } else if (projectType == 'Maven') {
            steps.echo 'Running SonarQube analysis (Maven)...'
            steps.sh """
                cd ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}

                mvn clean verify sonar:sonar \\
                -Dsonar.projectKey=${sonarProjectKey} \\
                -Dsonar.host.url=${sonarHostUrl} \\
                -Dsonar.login=${sonarToken}
            """
        }
    } else {
        steps.echo "SonarQube analysis skipped."
    }
}
