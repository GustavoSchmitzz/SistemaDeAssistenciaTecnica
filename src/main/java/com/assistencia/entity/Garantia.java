package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Garantia {
    private Integer id;
    private Integer diasDeGarantia;
    private OrdemDeServico ordemDeServico;
}
