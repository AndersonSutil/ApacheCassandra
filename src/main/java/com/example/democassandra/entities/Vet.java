package com.example.democassandra.entities;


import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Getter
@Table
public class Vet {
    @PrimaryKey
    private String id;

    private String nome_node;
    private Date data;
    private String nome;
    private int idade;

    public Vet() {
    }

    public Vet(String id, String nome_node, Date data, String nome, int idade) {
        this.id = id;
        this.nome_node = nome_node;
        this.data = data;
        this.nome = nome;
        this.idade = idade;
    }

    public Vet(String id, String nome_node, String nome, int idade) {
        this.nome_node = nome_node;
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$s, name = %3$s, age = %4$d }", getClass().getName(), getId(),
                getNome(), getIdade());
    }
}
