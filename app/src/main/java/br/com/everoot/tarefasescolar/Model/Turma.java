package br.com.everoot.tarefasescolar.Model;

import java.util.List;

public class Turma {
    private String id;
    private String nome;
    private String numero;
    private List<Tarefa> tarefas;
    private String idAdmin;

    public Turma() {}

    public Turma(String id, String nome, String numero, List<Tarefa> tarefas, String idAdmin) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.tarefas = tarefas;
        this.idAdmin = idAdmin;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String toString(){
        return getNome()+", "+getNumero();
    }
}
