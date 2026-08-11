package com.assistencia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "entrega_ordem_de_servico")
public class EntregaOrdemDeServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;

    @OneToOne
    @JoinColumn(name = "id_ordem_de_servico")
    private OrdemDeServico  ordemDeServico;
}
