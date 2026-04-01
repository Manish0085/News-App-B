pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "manishk57107/weather-app:v1"
        CONTAINER_NAME = "weather-app"
    }
    stages {
        stage ('Checkout code') {
            steps {
                git branch: 'main', url: 'https://github.com/Manish0085/Weather-App-Backend.git'
            }
        }
        stage ('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean install -DskipTests'
            }
        }
        stage ('Docker Build') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }
        stage ('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'   
                    }
            }
        }
        
        stage ('Push to DockerHub') {
            steps {
                sh 'docker push $DOCKER_IMAGE'
            }
        }
        stage ('Deploy') {
            steps {
                withCredentials([string(credentialsId: 'weather-api-key', variable: 'API_KEY')]){
                    sh '''
                        docker stop $CONTAINER_NAME || true
                        docker rm $CONTAINER_NAME || true
                        
                        docker run -p 8081:8080 \
                        --name $CONTAINER_NAME \
                        -e WEATHER_APIKEY=$API_KEY \
                        $DOCKER_IMAGE
                    '''
                }
            }
        }
    }
}
