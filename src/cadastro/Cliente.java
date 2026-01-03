package cadastro;

import java.time.LocalDateTime;

public class Cliente {

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime criadoEm;

    public Cliente() {
    }

    public Cliente(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }


    public Cliente(Integer id, String nome, String email, String telefone, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email =email;
        this.telefone = telefone;
        this.criadoEm = criadoEm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString(){
        return "Cliente{id="+id +
                ",nome='" +nome+ '\'' +
                ",email='" +email+ '\'' +
                ",telefone='" +telefone+ '\'' +
                ",criadoEm='" +criadoEm+ '}';
    }
}