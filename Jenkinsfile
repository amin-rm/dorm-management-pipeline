pipeline {
    agent any

    environment {
        // Jenkins credentials
        SONARQUBE_CREDS = credentials('sonarqube-credentials')  // SonarQube username & password
        DOCKER_CREDS = credentials('docker-credentials')  // DockerHub credentials
        // vars
        GIT_BRANCH = 'master'
        SLACK_CHANNEL = '#cicd-pipeline'
    }

    stages {
        stage('Clone Repository') {
            steps {
                echo 'Pulling changes...'
                git url: 'https://github.com/amin-rm/dorm-management-pipeline', branch: $GIT_BRANCH
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
                    sh "mvn sonar:sonar -Dsonar.login=$SONARQUBE_CREDS_USR -Dsonar.password=$SONARQUBE_CREDS_PSW"
                }
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
                    sh 'docker tag foyer-app:latest ${DOCKER_CREDS_USR}/dorm-management:latest'
                    // Login using the Personal Access Token
                    sh 'echo $DOCKER_CREDS_PSW | docker login -u $DOCKER_CREDS_USR --password-stdin'
                    sh 'docker push ${DOCKER_CREDS_USR}/dorm-management:latest'
                }
            }
        }

        stage('Notify Slack team') {
                    steps {
                        script {
                            echo 'Creating JaCoCo report zip...'
                            dir('foyer/target/site/jacoco') {
                                // Create a zip file of the JaCoCo report
                                sh 'zip -r jacoco-report.zip *'
                            }

                            // Define the build status and path to the zip file
                            def buildStatus = currentBuild.currentResult ?: 'SUCCESS'

                            // send zip file to Slack
                            dir('foyer/target/site/jacoco') {
                                slackUploadFile(
                                channel: $SLACK_CHANNEL,
                                filePath: "jacoco-report.zip",
                                initialComment: "Build Status: ${buildStatus}. Please find the JaCoCo code report attached."
                                )
                            }


                        }
                    }
        }

    }


}
