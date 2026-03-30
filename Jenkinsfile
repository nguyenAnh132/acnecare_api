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
            defaultValue: '.',
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
                    if [ ! -w '${env.DEPLOY_DIR}' ]; then
                        echo "ERROR: Không có quyền ghi vào ${env.DEPLOY_DIR}. Chạy: sudo chown -R jenkins:jenkins ${env.DEPLOY_DIR}"
                        exit 1
                    fi
                    rsync -a --delete --no-owner --no-group --exclude '.env' --exclude 'uploads/' '${env.WORKSPACE}/${params.SOURCE_REL_PATH}/' '${env.DEPLOY_DIR}/'
                """
            }
        }
        stage('Docker: build & deploy') {
            steps {
                sh """
                    set -e
                    cd '${env.DEPLOY_DIR}'
                    docker compose -f Docker-compose.yml pull mysql-db redis-cache
                    docker compose -f Docker-compose.yml up -d --build --force-recreate app
                """
            }
        }

        stage('Verify') {
            steps {
                sh """
                    set -e
                    cd '${env.DEPLOY_DIR}'
                    docker compose -f Docker-compose.yml ps
                """
            }
        }
    }

    post {
        failure {
            echo 'Pipeline thất bại. Lỗi docker.sock: sudo usermod -aG docker jenkins && sudo systemctl restart jenkins (rồi build lại).'
        }
    }
}
