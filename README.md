# Jakarta Faces + PrimeFaces 15 (Tomcat 11 & Java 21)

Este repositório contém um projeto base ("starter") configurado para rodar aplicações modernas utilizando **Jakarta EE 10** em um container **Tomcat 11**, utilizando **Java 21** e **PrimeFaces 15**.

O diferencial deste projeto é a configuração manual do CDI (Contexts and Dependency Injection) utilizando o **Weld**, necessária porque o Tomcat é um Servlet Container e não um servidor de aplicação completo (como WildFly), não possuindo CDI nativo.

## 🛠 Tecnologias e Versões

As versões foram definidas no `pom.xml` e `Dockerfile`:

* **Java:** 21 (LTS) - via Eclipse Temurin
* **Servidor:** Apache Tomcat 11.0 (Servlet 6.0)
* **Jakarta EE:** 10.0 (Web Profile API)
* **Jakarta Faces (JSF):** 4.1.4 (Implementação Mojarra)
* **PrimeFaces:** 15.0.10 (Tema Saga)
* **CDI Provider:** Weld Servlet 6.0.3.Final
