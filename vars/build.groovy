#!/usr/bin/groovy

def call(String projectType, String prodName) {
    def steps = this.steps
    def env = this.env

    String warPath = ''

    if (projectType == 'Gradle') {
        warPath = 'build/libs'
        steps.echo 'Building Project Artifact using Gradle'
        steps.withEnv(["GRADLE_OPTS=-Xmx4096m -Dorg.gradle.daemon=true -Dorg.gradle.jvmargs='-Xmx4096m'"]) {
            steps.sh 'chmod +x gradlew'
            steps.sh '''
                ./gradlew assemble -Dorg.gradle.java.home="/usr/lib/jvm/java-11-openjdk"
            '''
        }
    } else if (projectType == 'Maven') {
        warPath = 'target'
        steps.echo 'Building Project Artifact using Maven...'
        steps.dir("${env.JENKINS_HOME}/workspace/${env.JOB_NAME}") {
            steps.sh 'mvn clean package install -f pom.xml'
        }
    }

    String warFile = steps.sh(script: "ls ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/*.war | head -n 1", returnStdout: true).trim()

    steps.sh """
        echo "Source file: ${warFile}"
        echo "Destination file: ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war"

        if [ "${warFile}" != "${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war" ]; then
            mv "${warFile}" "${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war"
            echo "File renamed successfully."
        else
            echo "File name is perfect, no need to change"
        fi

        ls -lah ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/
    """

    String namePath = "${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/*.war"
    String warFileName = this.getName(namePath)

    return warFileName; //returning warFileName value from the environment.
}
