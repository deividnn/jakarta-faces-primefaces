/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dnn.service;

import com.dnn.model.Task;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class TaskService implements Serializable { // Adicione implements Serializable

    private static final long serialVersionUID = 1L;
    // URL do H2 em Memória (os dados somem ao reiniciar o Tomcat)
    private static final String JDBC_URL = "jdbc:h2:file:/data/tasksdb;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @PostConstruct
    public void init() {
        // Cria a tabela ao iniciar a aplicação
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver H2 não encontrado! Verifique o pom.xml", e);
        }
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS task ("
                    + "id IDENTITY PRIMARY KEY, "
                    + "titulo VARCHAR(255), "
                    + "descricao VARCHAR(500), "
                    + "status VARCHAR(50), "
                    + "data_prazo DATE)";
            stmt.execute(sql);

            // Insere dados iniciais se estiver vazio
            if (listarTodas().isEmpty()) {
                salvar(new Task(null, "Aprender Jakarta EE 10", "Estudar CDI e JSF", "Em Andamento", LocalDate.now().plusDays(5)));
                salvar(new Task(null, "Configurar Docker", "Criar Dockerfile otimizado", "Concluido", LocalDate.now().minusDays(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public ArrayList<Task> listarTodas() {
        ArrayList<Task> lista = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY id DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Task(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getString("status"),
                        rs.getObject("data_prazo", LocalDate.class)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void salvar(Task t) {
        try (Connection conn = getConnection()) {
            if (t.getId() == null) {
                String sql = "INSERT INTO task (titulo, descricao, status, data_prazo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, t.getTitulo());
                    ps.setString(2, t.getDescricao());
                    ps.setString(3, t.getStatus());
                    ps.setObject(4, t.getDataPrazo());
                    ps.executeUpdate();
                }
            } else {
                String sql = "UPDATE task SET titulo=?, descricao=?, status=?, data_prazo=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, t.getTitulo());
                    ps.setString(2, t.getDescricao());
                    ps.setString(3, t.getStatus());
                    ps.setObject(4, t.getDataPrazo());
                    ps.setLong(5, t.getId());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM task WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
