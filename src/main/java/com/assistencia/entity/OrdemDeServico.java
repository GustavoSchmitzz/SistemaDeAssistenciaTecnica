package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdemDeServico {
    private Integer id;
    private LocalDate dataInicio;
    private double valorServico;
    private Tecnico tecnico;
    private Peca peca;
    private StatusServico statusServico;
}