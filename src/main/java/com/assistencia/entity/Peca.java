package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Peca {
    private Integer id;
    private String Nome;
    private double valor;
    private Integer estoque;
    private Fornecedor fornecedor;
}
