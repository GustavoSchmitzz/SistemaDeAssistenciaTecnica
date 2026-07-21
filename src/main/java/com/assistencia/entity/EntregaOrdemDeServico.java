package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EntregaOrdemDeServico {
    private Integer id;
    private Date dataEntrega;
    private OrdemDeServico  ordemDeServico;
}
