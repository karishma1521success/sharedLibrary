def call(String namePath) {
    String warFileName = sh(script: "basename ${namePath}", returnStdout: true).trim()
    steps.echo "New WAR file: ${warFileName}"
    // env.NEW_WAR_FILE = warFileName
    steps.echo "Value for warFileName -> $warFileName"

    return warFileName;   //return the value of warFileName
}
