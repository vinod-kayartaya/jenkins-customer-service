pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "vinod/customer-service"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Compile') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test -Dtest="*Test" -DfailIfNoTests=false'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build') {
            steps {
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Create Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Test Docker Container') {
            steps {
                // Run the container with test profile so it uses H2 and we don't need MySQL.
                // We omit port mapping so it doesn't conflict on the host.
                sh "docker run -d --name test-container-${BUILD_NUMBER} -e SPRING_PROFILES_ACTIVE=test ${DOCKER_IMAGE}:${DOCKER_TAG}"
                
                // Give it some time to start up
                sh "sleep 15"
                
                // Test the endpoints by executing wget directly INSIDE the running container.
                // This avoids needing to pull external images like curlimages/curl and completely bypasses networking issues!
                sh "docker exec test-container-${BUILD_NUMBER} wget -qO- http://localhost:8080/api/customers || exit 1"
            }
            post {
                always {
                    // Clean up the test container
                    sh "docker stop test-container-${BUILD_NUMBER} || true"
                    sh "docker rm test-container-${BUILD_NUMBER} || true"
                }
            }
        }

        stage('Integration Test') {
            steps {
                // Run integration tests using H2 db
                sh './mvnw test -Dtest="*IntegrationTest" -DfailIfNoTests=false'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }
    }
}
