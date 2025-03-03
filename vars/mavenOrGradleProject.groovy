def call() {
    def steps = this.steps  
    def env = this.env  //fetching all environment variables in jenkins pipeline.

    steps.sh '''
        java -version
        javac -version
    '''
    steps.echo 'check Build Tool (Gradle or Maven....... )'
    env.projectType = ''
    if (steps.fileExists('build.gradle')) {
        env.projectType = 'Gradle'
        steps.echo 'Gradle Project Detected'
        steps.sh 'gradle -version'
        steps.echo "env.projectType set to: ${env.projectType}" // Debugging
    }else if (steps.fileExists('pom.xml')) {
        env.projectType = 'Maven'
        steps.echo 'Maven project detected'
        steps.echo "env.projectType set to: ${env.projectType}" // Debugging
    }else {
        steps.error 'No build file found! Not a valid Gradle or Maven project.'
    }

    steps.echo "Project type within shared library: ${env.projectType}" // Verification
}
