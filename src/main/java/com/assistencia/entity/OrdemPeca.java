package com.assistencia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ordens_peca")
public class OrdemPeca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "id_ordem_servico", nullable = false)
    private OrdemDeServico ordemDeServico;

    @OneToOne
    @JoinColumn(name = "id_peca", nullable = false)
    private Peca peca;
}
