package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagamento {
    private Integer id;
    private String formaPagamento;
    private OrdemDeServico ordemDeServico;
}
