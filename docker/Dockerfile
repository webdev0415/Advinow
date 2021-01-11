FROM openjdk:8

RUN mkdir /opt/tomcat/
WORKDIR /opt/tomcat

COPY docker/apache-tomcat-9.0.38.tar.gz .
RUN tar xvfz apache-tomcat-9.0.38.tar.gz

RUN mv apache-tomcat-9.0.38/* /opt/tomcat/
# Delete tomcat files
RUN rm -rf  /opt/tomcat/bin/*.bat \
  && rm -rf /opt/tomcat/webapps/docs \
  && rm -rf /opt/tomcat/webapps/examples \
  && rm -rf /opt/tomcat/webapps/manager \
  && rm -rf /opt/tomcat/webapps/host-manager \
  && rm -rf /opt/tomcat/webapps/ROOT/* \
  && rm -rf /tmp/* \
  && rm -rf /var/cache/*

COPY ./target  /opt/tomcat/webapps
RUN mkdir /opt/micacache

WORKDIR /opt/tomcat/webapps

COPY docker/entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
