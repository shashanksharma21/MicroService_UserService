pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages{
        
        stage("Test"){
            steps{
                echo "======== Test ========"
                bat "mvn test"
            }
        }
        stage("Build"){
            steps{
                echo "======== build ========"
                bat "mvn package"
            }
        }
        stage("Docker Image"){
            steps{
                echo "======== building docker image ========"
                bat "docker build -t user-service ."
            }
        }
        
        stage("run DockerContainer") {
            steps {
                echo "======== running docker image as user-serviceContainer ========"
                bat "docker-compose up -d"
            }
        }
    }
}
