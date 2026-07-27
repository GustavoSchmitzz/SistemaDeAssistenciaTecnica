package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Garantia {
    private int id;
    private int diasDeGarantia;
    private OrdemDeServico ordemDeServico;
}
