pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
                echo "Checkout completed."
            }
        }

        stage('Build') {
            steps {
                dir('telemetry-collector') {
                    sh './mvnw clean package -DskipTests'
                }
                echo "telemetry-collector build completed."

                dir('telemetry-aggregator') {
                    sh './mvnw clean package -DskipTests'
                }
                echo "telemetry-aggregator build completed."

                dir('incident-engine') {
                    sh './mvnw clean package -DskipTests'
                }
                echo "incident-engine build completed."

                echo "All Maven builds completed."
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker images for Jenkins build #${BUILD_NUMBER}..."

                sh 'docker build -t pipeline-guard/collector:${BUILD_NUMBER} ./telemetry-collector'
                sh 'docker build -t pipeline-guard/aggregator:${BUILD_NUMBER} ./telemetry-aggregator'
                sh 'docker build -t pipeline-guard/incident-engine:${BUILD_NUMBER} ./incident-engine'

                echo "Docker images built successfully."
            }
        }

       stage('Prepare Environment') {
           steps {
               sh 'cp /var/jenkins_home/deployment/.env-collector telemetry-collector/.env'
               echo "telemetry-collector .env copied"

               sh 'cp /var/jenkins_home/deployment/.env-aggregator telemetry-aggregator/.env'
               echo "telemetry-aggregator .env copied"

               sh 'cp /var/jenkins_home/deployment/.env-engine incident-engine/.env'
               echo "incident-engine .env copied"
           }
       }

        stage('Deploy') {
            steps {
                echo "Stopping existing containers and removing volumes..."

                sh 'docker compose down -v'

                echo "Starting application containers..."

                sh 'docker compose up -d --build'

                echo "Deployment completed successfully."
                echo "Deployment is running on Jenkins build #${BUILD_NUMBER}."
            }
        }
    }
}