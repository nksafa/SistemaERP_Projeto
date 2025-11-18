# 1. Usa uma imagem base com Maven e Java 11 para construir o projeto
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY . .
# Compila o projeto e gera o .war (pula testes para ser mais rápido)
RUN mvn clean package -DskipTests

# 2. Usa uma imagem base do Tomcat 9 (compatível com javax) para rodar
FROM tomcat:9.0-jdk11-openjdk
WORKDIR /usr/local/tomcat/webapps/

# 3. Copia o .war gerado no passo anterior para dentro do Tomcat
# IMPORTANTE: Renomeamos para ROOT.war para que abra direto no link (sem /ProjetoPOO3)
COPY --from=build /app/target/ProjetoPOO3.war ./ROOT.war

# 4. Expõe a porta padrão do Tomcat
EXPOSE 8080

# 5. Inicia o Tomcat
CMD ["catalina.sh", "run"]