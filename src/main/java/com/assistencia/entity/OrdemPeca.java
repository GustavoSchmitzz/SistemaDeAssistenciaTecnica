package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdemPeca {
    private Integer id;
    private Integer quantidade;
    private OrdemDeServico ordemDeServico;
    private Peca peca;
}
