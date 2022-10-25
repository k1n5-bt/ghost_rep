FROM python:3.8

USER root

COPY ./dashboard ./dashboard

RUN cd ./dashboard && \
    pip install -r requirements.txt && \
    apt-get -y update && \
    apt-get -y install gunicorn

WORKDIR ./dashboard

ENV HOST 0.0.0.0
ENV PORT 8085

EXPOSE 8085

ENTRYPOINT python main.py