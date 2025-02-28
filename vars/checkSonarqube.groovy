def checkSonarAvailability(boolean useSonar, String sonarqubeIP, String sonarqubePort) {
    def steps = this.steps
    if (useSonar) { // Directly use boolean value
        steps.echo "Checking Sonar... ${sonarqubeIP}:${sonarqubePort}"
        steps.timeout(time: 10, unit: 'SECONDS') {
            try {
                steps.waitUntil {
                    steps.sh(script: "nc -z ${sonarqubeIP} ${sonarqubePort}", returnStatus: true) == 0
                }
                steps.echo 'Sonar OK'
            } catch (e) {
                steps.error "Sonar failed: ${e}"
            }
        }
    }
}
