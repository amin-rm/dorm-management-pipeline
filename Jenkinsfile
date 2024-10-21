pipeline {
    agent any

    environment {
        // Jenkins credentials
        SONARQUBE_CREDS = credentials('sonarqube-credentials')  // SonarQube username & password
        DOCKER_CREDS = credentials('docker-credentials')  // DockerHub credentials
        GIT_CREDENTIALS = credentials('github-creds')  // GitHub credentials
        // vars
        APP_VERSION = '1.0.0' // define your application version
        GIT_EMAIL = "ramdhaniahmedamine@gmail.com"
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
                    sh "mvn deploy -DskipTests -Drevision=${env.APP_VERSION}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t foyer-app:${env.APP_VERSION} ."
            }
        }



        stage('Push Docker Image to DockerHub') {
            steps {
                echo 'Tagging and pushing Docker image to DockerHub...'
                script {
                    sh '''
                        DOCKER_USER=${DOCKER_CREDS_USR}
                        DOCKER_PASS=${DOCKER_CREDS_PSW}
                        docker tag foyer-app:${env.APP_VERSION} ${DOCKER_USER}/dorm-management:${env.APP_VERSION}
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${DOCKER_USER}/dorm-management:${env.APP_VERSION}
                    '''
                }
            }
        }


        stages {
            stage('Update helm chart') {
                steps {
                    echo 'Updating helm chart...'
                    dir('helm-charts/dorm-backend-app') {
                        // Update the image version in values.yaml
                        sh "sed -i 's/tag: .*/tag: ${env.APP_VERSION}/' values.yaml"

                        sh """
                        git config user.email ${GIT_EMAIL}
                        git config user.name ${GIT_CREDENTIALS_USR}
                        git add values.yaml
                        git commit -m "Update Helm chart tag to ${env.APP_VERSION}"
                        git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/amin-rm/dorm-management-pipeline.git HEAD:${env.GIT_BRANCH}
                    """
                    }
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
