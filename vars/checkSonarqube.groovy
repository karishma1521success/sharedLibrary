def call(boolean useSonar, String sonarqubeIP, String sonarqubePort) {
    if (useSonar == 'true') {
        // Check SonarQube Server availability
        echo "SonarQube IP: ${env.sonarqube_ip}, Port: ${env.sonarqube_port}"
        netCheck(env.sonarqube_ip, env.sonarqube_port, 'SonarQube')
        echo "Sonarqube is reachable"
    }
}
