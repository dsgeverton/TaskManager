package br.com.everoot.tarefasescolar.Model;

public class Tarefa {

    private String id;
    private String descricao;
    private String data;
    private String hora;
    private String local;
    private String turmaID;

    public String getTurmaID() {
        return turmaID;
    }

    public void setTurmaID(String turmaID) {
        this.turmaID = turmaID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}

