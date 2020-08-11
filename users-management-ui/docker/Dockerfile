FROM httpd:2.4-alpine

LABEL maintainer="florian.lopes@outlook.com"

RUN rm -r /usr/local/apache2/htdocs/*

COPY ./dist/users-management-ui/ /usr/local/apache2/htdocs/
