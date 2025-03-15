#!/bin/bash
source ./docker.properties
export COMPOSE_PROFILES=test
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"

# Передаем аргумент скрипта в переменную окружения
export BROWSER=${1:-chrome}  # По умолчанию "chrome", если аргумент не передан

export ALLURE_DOCKER_API=http://allure:5050/
export HEAD_COMMIT_MESSAGE="local build"
export ARCH=$(uname -m)

echo '### Java version ###'
java --version

# Проверяем, установлен ли флаг SKIP_BUILD
if [ -z "$SKIP_BUILD" ]; then
  echo "### SKIP_BUILD not set, proceeding with full build and cleanup ###"

  # Останавливаем и удаляем контейнеры
  docker compose down
  docker_containers=$(docker ps -a -q)
  docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')

  if [ ! -z "$docker_containers" ]; then
    echo "### Stop containers: $docker_containers ###"
    docker stop $docker_containers
    docker rm $docker_containers
  fi

  if [ ! -z "$docker_images" ]; then
    echo "### Remove images: $docker_images ###"
    docker rmi $docker_images
  fi

  # Собираем проект
  echo '### Java version ###'
  java --version
  bash ./gradlew clean
  bash ./gradlew jibDockerBuild -x :niffler-e-2-e-tests:test
else
  echo "### SKIP_BUILD is set, skipping build and cleanup ###"
fi

# Запускаем контейнеры
docker pull selenoid/vnc_chrome:127.0
docker pull selenoid/firefox:125.0
docker compose -f docker-compose.test.yml up -d
docker ps -a