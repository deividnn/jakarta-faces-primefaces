/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dnn.controller;

import com.dnn.model.Task;
import com.dnn.service.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class TaskController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TaskService service;

    @Getter
    @Setter
    private ArrayList<Task> tasks;

    @Getter
    @Setter
    private Task taskSelecionada;

    @Inject
    private transient FacesContext facesContext;

    @PostConstruct
    public void init() {
        this.tasks = service.listarTodas();
    }

    public void novaTask() {
        this.taskSelecionada = new Task();
        this.taskSelecionada.setStatus("Pendente"); // Padrão
    }

    public void salvarTask() {
        service.salvar(taskSelecionada);
        this.tasks = service.listarTodas(); // Atualiza a lista
        facesContext.addMessage(null, new FacesMessage("Sucesso", "Task salva!"));
        PrimeFaces.current().executeScript("PF('dialogTask').hide()");
        PrimeFaces.current().ajax().update("form:dt-tasks", "form:messages");
    }

    public void removerTask() {
        service.deletar(taskSelecionada.getId());
        this.tasks = service.listarTodas();
        facesContext.addMessage(null, new FacesMessage("Removido", "Task excluída."));
        PrimeFaces.current().ajax().update("form:dt-tasks", "form:messages");
    }
}
