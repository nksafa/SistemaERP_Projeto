# ETAPA 1: Build (Construção)
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY . .
# Compila o projeto
RUN mvn clean package -DskipTests

# ETAPA 2: Run (Execução)
# Usamos uma versão atualizada do Tomcat 9 com Java 11
FROM tomcat:9-jdk11

# --- CORREÇÃO AQUI ---
# Removemos os aplicativos padrão do Tomcat (a pasta ROOT antiga)
# para evitar conflito com o seu projeto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o seu WAR para a pasta webapps como ROOT.war
COPY --from=build /app/target/ProjetoPOO3-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta
EXPOSE 8080

# Inicia o servidor
CMD ["catalina.sh", "run"]