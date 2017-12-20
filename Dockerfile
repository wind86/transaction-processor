FROM cogniteev/oracle-java

EXPOSE 8080

RUN apt-get update && apt-get install -y git
RUN apt-get install -y maven

CMD git clone https://github.com/wind86/transaction-processor.git \ 
 && cd transaction-processor && git checkout store-transaction-details \
 && mvn clean package && sh start.sh