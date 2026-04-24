pipeline {
    agent any

    environment {
    	DOCKER_IMG_NAME = 'user-service'
    	DOCKER_TMP_CONTAINER_NAME = 'tmp-user-service-container'
    	DOCKER_REPO = 'learnwithvinod'
    }

    stages {
    
        stage('clean') {
            steps {
                sh 'mvn clean' 
            }
        }
        
        stage('compile') {
            steps {
            	echo 'compiling source files...'
                sh 'mvn compile' 
            }
        }
        
                
        stage('unit-test') {
            steps {
            	echo 'running unit tests...'
                sh 'mvn test' 
            }
        }
        
        stage('build') {
            steps {
            	echo 'creating the JAR for the project...'
                sh 'mvn package -DskipTests=true' 
            }
        }
        
        
    }
    

}










