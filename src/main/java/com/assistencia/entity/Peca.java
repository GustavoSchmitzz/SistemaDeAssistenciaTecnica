package com.assistencia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pecas")
public class Peca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nome",nullable = false,length = 100)
    private String nome;

    @Column(name = "valor", nullable = false)
    private double valor;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @OneToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;
}
