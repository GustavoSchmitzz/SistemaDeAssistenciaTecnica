package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PecaComDefeito {
    private Integer id;
    private String tipoPeca;
    private String marca;
    private String modelo;
    private String descricao;
    private Cliente cliente;
}
