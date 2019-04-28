FROM maven:3.3-jdk-8
COPY . /app
WORKDIR /app
# install dependencies
# RUN mvn dependency:resolve
# build the app
RUN mvn clean compile assembly:single
WORKDIR /app/target
# java -cp /app/target/*.jar fr.lefuturiste.urlshortener.App
CMD ["sh", "/app/start.sh"]
