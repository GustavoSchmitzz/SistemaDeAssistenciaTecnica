package com.assistencia.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ordem_de_servico")
public class OrdemDeServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "valor_servico", nullable = false)
    private double valorServico;

    @OneToOne
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "id_peca_com_defeito", nullable = false)
    private PecaComDefeito peca;

    @OneToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusServico statusServico;

    @OneToOne
    @JoinColumn(name = "id_garantia", nullable = false)
    private Garantia garantia;

    @OneToOne
    @JoinColumn(name = "id_pagamento", nullable = false)
    private Pagamento  pagamento;
}