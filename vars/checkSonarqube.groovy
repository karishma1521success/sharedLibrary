def call(boolean useSonar, String sonarqubeIp, String sonarqubePort) {
    if (useSonar) {
        steps.echo "SonarQube IP: ${sonarqubeIp}, Port: ${sonarqubePort}"
        netCheck(sonarqubeIp, sonarqubePort, 'SonarQube')
    }
}

def netCheck(String host, String port, String serviceName) {
    def steps = this.steps
    timeout(time: 10, unit: 'SECONDS') {
        try {
            steps.waitUntil {
                steps.sh(script: "nc -z ${host} ${port}", returnStatus: true) == 0
            }
            steps.echo "${serviceName} is reachable at ${host}:${port}"
        } catch (e) {
            steps.error "${serviceName} is not reachable at ${host}:${port}. Error: ${e.getMessage()}"
        }
    }
}
