pipeline {
    agent any

    environment {
        // Jenkins credentials
        SONARQUBE_CREDS = credentials('sonarqube-credentials')  // SonarQube username & password
        DOCKER_CREDS = credentials('docker-credentials')  // DockerHub credentials
        // vars
        GIT_BRANCH = 'chambre-management'
        SLACK_CHANNEL = '#cicd-pipeline'
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo 'Pulling changes...'
                git url: 'https://github.com/amin-rm/dorm-management-pipeline', branch: env.GIT_BRANCH
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building project...'
                dir('foyer') {
                    sh "mvn clean install -DskipTests"
                }
            }
        }

        stage('Unit tests') {
            steps {
                echo 'Performing unit tests...'
                dir('foyer') {
                    sh "mvn test"
                }
            }
        }

        stage('Run SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis...'
                dir('foyer') {
                    // sh "mvn sonar:sonar -Dsonar.login=${env.SONARQUBE_CREDS_USR} -Dsonar.password=${env.SONARQUBE_CREDS_PSW}"
                    withSonarQubeEnv('SonarQube') {
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }

        stage("Quality gate") {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }

        stage('Deploy to nexus') {
            steps {
               echo 'Deploying to nexus...'
               dir('foyer') {
                sh 'mvn deploy -DskipTests'
               }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                dir('foyer') {
                    sh 'docker build -t foyer-app:latest .'
                }
            }
        }



        stage('Push Docker Image to DockerHub') {
            steps {
                echo 'Tagging and pushing Docker image to DockerHub...'
                script {
                    sh '''
                        DOCKER_USER=${DOCKER_CREDS_USR}
                        DOCKER_PASS=${DOCKER_CREDS_PSW}
                        docker tag foyer-app:latest ${DOCKER_USER}/dorm-management:latest
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKER_USER}/dorm-management:latest
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Creating JaCoCo report zip...'
            dir('foyer/target/site/jacoco') {
                // Create a zip file of the JaCoCo report
                sh 'zip -r jacoco-report.zip *'
            }

            script {
                // Define the build status and path to the zip file
                def buildStatus = currentBuild.currentResult ?: 'SUCCESS'

                // send zip file to Slack
                dir('foyer/target/site/jacoco') {
                    slackUploadFile(
                            channel: env.SLACK_CHANNEL,
                            filePath: "jacoco-report.zip",
                            initialComment: "Build Status: ${buildStatus}. Please find the JaCoCo code report attached."
                    )
                }
            }
        }
    }
}
