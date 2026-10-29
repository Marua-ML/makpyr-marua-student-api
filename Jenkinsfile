pipeline {
    agent { label 'docker' }

    tools {
        jdk 'jdk-21'
    }

    triggers {
        pollSCM('H/3 * * * *')
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        STUDENT = 'Marua Makpyr'
        STUDENT_ID = '37578'
        IMAGE = 'makpyr-marua-student-api'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    def version = sh(script: "grep -E \"^version = \" build.gradle | cut -d\\' -f2", returnStdout: true).trim()
                    env.TAG = "v${version}-makpyr-marua-${env.STUDENT_ID}"
                    def safeBranch = env.BRANCH_NAME.replaceAll('[^a-zA-Z0-9]', '-').toLowerCase()
                    env.RUN_ID = "makpyr-marua-${safeBranch}-${env.BUILD_NUMBER}"
                }
                echo "Build of ${STUDENT} (ID ${STUDENT_ID})"
                echo "Branch: ${env.BRANCH_NAME}, image: ${IMAGE}:${TAG}"
                sh 'git log -1 --pretty="Last commit: %h %s (%an)"'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew --no-daemon clean bootJar'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE}:${TAG} .'
            }
        }

        stage('Verify') {
            steps {
                sh 'docker image inspect ${IMAGE}:${TAG} --format "Image ${IMAGE}:${TAG} exists, created {{.Created}}"'
                withCredentials([usernamePassword(credentialsId: 'makpyr-marua-db', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASSWORD')]) {
                    sh '''
                        docker network create ${RUN_ID}-net
                        docker run -d --name ${RUN_ID}-db --network ${RUN_ID}-net --network-alias postgres \
                            -e POSTGRES_DB=makpyr_marua_db -e POSTGRES_USER="$DB_USER" -e POSTGRES_PASSWORD="$DB_PASSWORD" \
                            postgres:16-alpine
                        for i in $(seq 1 30); do
                            docker exec ${RUN_ID}-db pg_isready -U "$DB_USER" -d makpyr_marua_db && break
                            sleep 2
                        done
                        docker run -d --name ${RUN_ID}-api --network ${RUN_ID}-net \
                            -e DB_URL=jdbc:postgresql://postgres:5432/makpyr_marua_db -e DB_USER="$DB_USER" -e DB_PASSWORD="$DB_PASSWORD" \
                            ${IMAGE}:${TAG}
                    '''
                }
                sh '''
                    for i in $(seq 1 40); do
                        INFO=$(docker exec ${RUN_ID}-api curl -fs http://localhost:8081/api/makpyr-marua/info) && break
                        sleep 3
                    done
                    echo "Info: $INFO"
                    echo "$INFO" | grep -q '"studentId":"37578"'
                    echo "Verify passed for ${STUDENT} (ID ${STUDENT_ID})"
                '''
            }
            post {
                always {
                    sh 'docker rm -f ${RUN_ID}-api ${RUN_ID}-db || true'
                    sh 'docker network rm ${RUN_ID}-net || true'
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS: pipeline of ${STUDENT} (ID ${STUDENT_ID}) finished, image ${IMAGE}:${TAG}"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT} (ID ${STUDENT_ID}) failed, see the stage above"
        }
    }
}
