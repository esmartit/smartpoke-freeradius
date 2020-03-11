FROM freeradius/freeradius-server:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y upgrade tzdata
ENV TZ=Europe/Madrid
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
