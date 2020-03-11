#!/bin/sh
docker login -u $DOCKER_USER -p $DOCKER_PASS
docker push esmartit/smartpoke-freeradius:"$1"
docker push esmartit/smartpoke-freeradius:latest
helm package smartpoke-freeradius --version "$1" --app-version "$1"
touch version.txt
echo "$1" >> version.txt
exit