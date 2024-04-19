node {
      stage('init') {
        checkout scm
      }
      stage('build') {
        sh 'mvn clean package'
      }
      stage('deploy') {
        withCredentials([azureServicePrincipal('azure_service_principal')]) {
          // Log in to Azure
          sh '''
            az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID
            az account set -s $AZURE_SUBSCRIPTION_ID
          '''
          // Set default resource group name and service name. Replace <resource group name> and <service name> with the right values
          sh 'az config set defaults.group=ADAMGR'
          sh 'az config set defaults.spring=avro-api-test'

          // Deploy applications
          sh 'az spring app deploy -n avro-bff --jar-path ./laElectronicsAdmin/api/target1/api-1.0.0.jar'
          sh 'az logout'
        }
      }
    }