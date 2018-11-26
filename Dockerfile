FROM openjdk:8-jre-alpine

RUN mkdir maven && mkdir maven/config && mkdir maven/fonts
COPY maven/ /maven/
COPY /src/main/scripts/artifact_runner.sh maven/
COPY /src/main/resources/logback.xml maven/config/
COPY /fonts/* maven/fonts/

EXPOSE ${project.service.port}

CMD cd /maven && ./artifact_runner.sh ${final-artifact-name}.jar
