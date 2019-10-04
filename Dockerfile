FROM ubuntu:latest

RUN apt-get update

RUN apt-get install -y  \
    openjdk-8-jdk maven

COPY . /root/socket-chat

WORKDIR /root/socket-chat
