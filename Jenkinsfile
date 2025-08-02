pipeline {
    agent any

    environment {
        PROJECT_DIR = "/opt/ozon-helper"
        BRANCH_NAME = "master"
    }

    stages {
        stage('Build & Deploy') {
            steps {
                script {
                    echo "Копируем обновления в рабочую директорию проекта"
                    sh """
                        # Останавливаем текущие контейнеры
                        docker-compose down

                        # Собираем образы без кэша
                        docker-compose build --no-cache

                        # Запускаем контейнеры в фоне
                        docker-compose up -d
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    echo "Проверяем, что приложение отвечает на /actuator/health"
                    sh """
                        sleep 10
                        curl -f http://127.0.0.1:1212/dev/bot/ozon/helper/actuator/health || exit 1
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Деплой успешен!"
        }
        failure {
            echo "Ошибка деплоя! Проверьте логи Jenkins."
        }
    }
}
