package com.assistencia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "peca_com_defeito")
public class PecaComDefeito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_peca", nullable = false, length = 30)
    private String tipoPeca;

    @Column(name = "marca", nullable = false, length = 30)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 30)
    private String modelo;

    @Column(name = "problema", nullable = false, length = 255)
    private String problema;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
