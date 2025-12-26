# ==========================================
# ESTÁGIO 1: Build (Compilação com Maven)
# ==========================================
# Usamos uma imagem Maven que já tem o JDK 21 (Eclipse Temurin) instalado
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container de build
WORKDIR /app

# 1. Copia apenas o pom.xml primeiro (Cache de Dependências)
# Isso faz com que o Docker não baixe todas as dependências de novo se você só mudar o código Java.
COPY pom.xml .

# Baixa as dependências e plugins (modo offline para preparar o cache)
# Se der erro aqui na primeira vez, pode remover esta linha, mas ela acelera builds futuros.
RUN mvn dependency:go-offline

# 2. Copia o código fonte do projeto
COPY src ./src

# 3. Compila o projeto e gera o .war
# O flag -DskipTests agiliza o build (opcional, pode remover se quiser rodar testes)
RUN mvn clean package -DskipTests

# ==========================================
# ESTÁGIO 2: Runtime (Tomcat 11 + JDK 21)
# ==========================================
# Usamos a imagem oficial do Tomcat 11 rodando no JDK 21
FROM tomcat:11.0-jdk21

# Remove as aplicações padrão do Tomcat para segurança e limpeza
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o .war gerado no estágio anterior para a pasta de deploy
# Renomeamos para ROOT.war para que a aplicação abra direto em http://localhost:8080/
# Se preferir o contexto, use: COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/meuapp.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta padrão
EXPOSE 8080

# O comando padrão do Tomcat já é iniciar o catalina, não precisa de CMD
#docker build -t deivid/primefaces-app .
#docker run -d -p 8080:8080 --name meu-app-jsf deivid/primefaces-app