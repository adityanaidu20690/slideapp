pipeline {
    agent {
        node {
            label 'maven'
        }
    }
environment {
    
    PATH = "/opt/apache-maven-3.9.4/bin:$PATH"
}
    stages {
	stage ('Git Checkout') {
  steps {
      git 'https://github.com/adityanaidu20690/slideapp.git'
     }
}
stage('maven') {
            steps {
                echo "---------build started-------------"
                sh 'mvn clean install -Dmaven.test.skip=true'
                echo "---------build completed-------------"
            }
        }
        stage('test') {
            steps {
                echo "---------unit test started-------------"
                sh 'mvn surefire-report:report'
                echo "---------unit test completed-------------"
            }
        }
        stage('SonarQube analysis') {
            environment {
    
    scannerHome = tool 'addytest'
}
            steps {
                withSonarQubeEnv('addytest') { // If you have configured more than one global server connection, you can specify its name
      sh "${scannerHome}/bin/sonar-scanner"
    }
            }
        }
        stage("Quality Gate"){
            steps{
                    script{
                        timeout(time: 1, unit: 'HOURS') {
              def qg = waitForQualityGate()
              if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
              }
          }
                    }
            }
          
        }
         stage('sast owasp') {
            steps {
              dependencyCheck additionalArguments: '''--project slideapp
--scan	/home/ec2-user/jenkins/workspace/slideapp
--format	XML''', odcInstallation: 'default'
            }
        }
        stage('Docker Build'){
          steps{
	sh '''docker build -t slidergame .
docker tag demo adityanaidu20690/slidergame:latest
'''
	}
    }
    stage ('Image scanning'){
        steps{
            sh '''trivy image adityanaidu20690/slidergame:latest'''
        }
    }
	stage('Docker Hub'){
          steps{
	sh '''
docker tag slidergame adityanaidu20690/slidergame:latest
docker push adityanaidu20690/slidergame:latest'''
	}
	}
    stage('Deploy'){
        steps{
            sh 'docker run -d -it --name demo -p 8085:8080 adityanaidu20690/slidergame:latest'
        }
    }

         stage('publish the report') {
            steps {
               dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

}
}

