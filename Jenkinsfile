pipeline {
  agent any

  tools {
    maven 'maven-latest' // EXACT name from Global Tool Configuration
  }

  environment {
    GIT_CREDENTIALS = 'git-creds' // the credential ID you added
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM',
          branches: [[name: '*/main']],              // change branch if needed
          doGenerateSubmoduleConfigurations: false,
          userRemoteConfigs: [[
            url: 'https://github.com/codewithabhi-ops/sample-java-webapp.git', // change
            credentialsId: env.GIT_CREDENTIALS
          ]]
        ])
      }
    }

    stage('Build') {
      steps {
        // Linux agent (sh). If on Windows agent use 'bat' instead.
        sh "mvn -B clean package"
      }

post {
  success {
    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
    script {
      if (fileExists('target/surefire-reports')) {
        junit 'target/surefire-reports/*.xml'
      } else {
        echo "No test reports found, skipping junit step"
      }
    }
  }
}


  post {
    always { echo "Pipeline finished: ${currentBuild.currentResult}" }
  }
}

