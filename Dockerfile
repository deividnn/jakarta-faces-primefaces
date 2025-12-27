# ==========================================
# ESTÁGIO 1: Build (Compilação com Maven)
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# 1. Copia pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia código e compila
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# ESTÁGIO 2: Runtime (Tomcat 11 + JDK 21)
# ==========================================
FROM tomcat:11.0-jdk21

# --- ATUALIZAÇÃO PARA PERSISTÊNCIA H2 ---
# 1. Cria a pasta /data onde o arquivo do banco ficará
RUN mkdir -p /data

# 2. Define essa pasta como um Volume (opcional, mas boa prática para documentação)
# Isso avisa ao Docker que esta pasta contém dados persistentes
VOLUME /data
# ----------------------------------------

# Remove as aplicações padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o .war gerado
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta padrão
EXPOSE 8080

# O comando padrão do Tomcat já é iniciar o catalina
## 1. Construir a nova imagem
#docker build -t deivid/primefaces-app .

# 2. Rodar mapeando o volume (Aqui acontece a mágica da persistência)
#docker run -d -p 8080:8080 -v h2_dados_volume:/data --name meu-app-jsf deivid/primefaces-app