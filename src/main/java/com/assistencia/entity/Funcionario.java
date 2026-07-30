package com.assistencia.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Funcionario {
    private Integer id;
    private String nome;
    private String telefone;
    private String especialidade;
    private String email;
    private String senha;
}
