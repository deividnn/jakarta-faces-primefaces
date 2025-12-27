/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dnn.model;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {

    private Long id;
    private String titulo;
    private String descricao;
    private String status; // "Pendente", "Em Andamento", "Concluido"
    private LocalDate dataPrazo;
}
