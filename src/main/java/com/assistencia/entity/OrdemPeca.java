package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemPeca {
    private int id;
    private int quantidade;
    private OrdemDeServico ordemDeServico;
    private Peca peca;
}
