pipeline {
  agent any

  tools {
    maven 'maven-latest' // EXACT name from Global Tool Configuration
  }

  environment {
    GIT_CREDENTIALS = 'git-creds'         // GitHub credentials ID
    TOMCAT_SSH = 'tomcat-ec2-ssh'         // SSH credentials ID in Jenkins
    TOMCAT_HOST = '<TOMCAT_EC2_PUBLIC_IP>' // Replace with your EC2 public IP
    APP_NAME = 'sample-java-webapp'       // The name of your WAR/application
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM',
          branches: [[name: '*/main']],
          doGenerateSubmoduleConfigurations: false,
          userRemoteConfigs: [[
            url: 'https://github.com/codewithabhi-ops/sample-java-webapp.git',
            credentialsId: env.GIT_CREDENTIALS
          ]]
        ])
      }
    }

    stage('Build') {
      steps {
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
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarQube') {
          sh 'mvn sonar:sonar -Dsonar.projectKey=sample-java-webapp -Dsonar.host.url=http://3.27.141.119:9000'
        }
      }
    }

    stage('Deploy to Tomcat via SSH') {
      steps {
        sshagent([env.TOMCAT_SSH]) {
          sh '''
            echo "Deploying WAR to Tomcat on ${TOMCAT_HOST}..."

            # Locate the built WAR
            WAR_FILE=$(ls target/*.war | head -n1)

            echo "Copying WAR file to remote server..."
            scp -o StrictHostKeyChecking=no $WAR_FILE ubuntu@${TOMCAT_HOST}:/tmp/deploy.war

            echo "Executing remote deployment script..."
            ssh -o StrictHostKeyChecking=no ubuntu@${TOMCAT_HOST} "sudo /opt/deploy/deploy_war.sh /tmp/deploy.war ${APP_NAME}"

            echo "Deployment to Tomcat completed successfully!"
          '''
        }
      }
    }
  }

  post {
    always {
      echo "Pipeline finished: ${currentBuild.currentResult}"
    }
  }
}
