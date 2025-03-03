def call() {
    def steps = this.steps  
    def env = this.env  //fetching all environment variables in jenkins pipeline.

    steps.sh '''
        java -version
        javac -version
    '''
    steps.echo 'check Build Tool (Gradle or Maven....... )'
    env.projectType = 'Maven'
    if (steps.fileExists('build.gradle')) {
        projectType = 'Gradle'
        steps.echo 'Gradle Project Detected'
        steps.sh 'gradle -version'
    }else if (fileExists('pom.xml')) {
        // projectType = 'Maven'
        steps.echo 'Maven project detected'
    }else {
        steps.error 'No build file found! Not a valid Gradle or Maven project.'
    }
}
