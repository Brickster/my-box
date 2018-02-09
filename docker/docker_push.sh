#! /bin/bash
echo "building brickster/my-box:$TRAVIS_TAG"
mv build/libs/my-box-1.0-SNAPSHOT.jar docker/app.jar
cd docker
docker build -t brickster/my-box -t brickster/my-box:$TRAVIS_TAG .
docker push brickster/my-box
