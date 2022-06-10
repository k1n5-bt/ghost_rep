FROM python:3.8

USER root

COPY ./redis_stor ./redis_stor

RUN cd ./redis_stor && \
    pip install -r requirements.txt && \
    apt-get -y update && \
    apt-get -y install gunicorn

WORKDIR ./redis_stor/app

ENV HOST 0.0.0.0
ENV PORT 8085
ENV REDIS_HOST 127.0.0.1
ENV REDIS_PORT 6379

EXPOSE 8085

ENTRYPOINT python main.py