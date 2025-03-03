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

    steps.sh """
        String warFile = \$(ls ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/*.war | head -n 1)
        steps.echo "Source file: \$warFile"
        steps.echo "Destination file: ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war"

        if [ "\$warFile" != "${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war" ]; then
            mv \$warFile ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/${prodName}.war
            steps.echo "File renamed successfully."
        else
            steps.echo "File name is perfect, no need to change"
        fi

        ls -lah ${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/
    """
    String namePath = "${env.JENKINS_HOME}/workspace/${env.JOB_NAME}/${warPath}/*.war"
    def warFileName = this.getName(namePath)
    return warFileName   //returning warFileName value
}
