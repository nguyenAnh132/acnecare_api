pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        DEPLOY_DIR = '/var/www/acnecare_api'
    }

    parameters {
        string(
            name: 'SOURCE_REL_PATH',
            defaultValue: '.i',
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Sync to deploy dir') {
            steps {
                sh """
                    set -e
                    mkdir -p '${env.DEPLOY_DIR}'
                    rsync -a --delete --exclude '.env' --exclude 'uploads/' '${env.WORKSPACE}/${params.SOURCE_REL_PATH}/' '${env.DEPLOY_DIR}/'
                """
            }
        }

        stage('Docker: build & deploy') {
            steps {
                dir("${env.DEPLOY_DIR}") {
                    sh '''
                        set -e
                        docker compose -f docker-compose.yml pull mysql-db redis-cache
                        docker compose -f docker-compose.yml up -d --build --force-recreate app
                    '''
                }
            }
        }

        stage('Verify') {
            steps {
                dir("${env.DEPLOY_DIR}") {
                    sh 'docker compose -f docker-compose.yml ps'
                }
            }
        }
    }

    post {
        failure {
            echo 'Pipeline thất bại'
        }
    }
}
