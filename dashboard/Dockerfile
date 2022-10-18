FROM python:3.8

USER root

COPY ./dashboard ./app

RUN cd ./app && \
    pip install -r requirements.txt && \
    apt-get -y update && \
    apt-get -y install gunicorn

WORKDIR ./app

ENV HOST 0.0.0.0
ENV PORT 8085

EXPOSE 8085

ENTRYPOINT python app.py