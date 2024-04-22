node {
    stage('init') {
        checkout scm
    }
    stage('Check Directory') {
                script {
                    // Print current working directory
                    println "Current directory is: ${pwd()}"
                }
    }
    stage('build') {
        bat 'mvn clean package'
    }
    stage('deploy') {
        withCredentials([azureServicePrincipal('2bbb6d8e-7697-45f9-8ff5-c6e87cb518f4')]) {
            // Log in to Azure
            bat '''
            az login --service-principal -u %AZURE_CLIENT_ID% -p %AZURE_CLIENT_SECRET% -t %AZURE_TENANT_ID%
            az account set -s %AZURE_SUBSCRIPTION_ID%
            '''
            // Set default resource group name and service name
            bat 'az config set defaults.group=ADAMGR'
            bat 'az config set defaults.spring=avro-api-test'

            // Deploy applications
            bat 'az spring app deploy -n avro-bff --jar-path ../laElectronicsAdmin/api/target/api-1.0.0.jar'
            bat 'az logout'
        }
    }
}
