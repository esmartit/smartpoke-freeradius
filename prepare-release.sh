#!/bin/sh
docker build -t esmartit/smartpoke-freeradius:"$1" -t esmartit/smartpoke-freeradius:latest .
exit