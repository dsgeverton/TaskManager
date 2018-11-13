package br.com.everoot.tarefasescolar.Model;

public class Usuario {
    private String token;
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private boolean isAdmin;
    // atributo para guarda turma em que o usuário está ligado
    private String idTurma;

    public Usuario() { }

    public Usuario(String token, String id, String nome, String sobrenome, String email, boolean isAdmin, String idTurma) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.isAdmin = isAdmin;
        this.idTurma = idTurma;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public String fullName(){
        return getNome()+" "+getSobrenome();
    }

}