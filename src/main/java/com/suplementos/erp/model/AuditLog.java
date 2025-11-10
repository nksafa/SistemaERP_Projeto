package com.suplementos.erp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "audit_logs")
public class AuditLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    private String usuarioResponsavel; // Guardamos o login do usuário
    private String acao;      // Ex: "SALVAR", "REMOVER"
    private String entidade;  // Ex: "Produto", "Cliente"

    @Lob // Permite textos longos
    private String detalhes;  // Ex: "Produto Whey ID 5 atualizado"

    public AuditLog() {}

    // Construtor utilitário para facilitar a criação
    public AuditLog(String usuarioResponsavel, String acao, String entidade, String detalhes) {
        this.dataHora = new Date(); // Pega a hora atual automaticamente
        this.usuarioResponsavel = usuarioResponsavel;
        this.acao = acao;
        this.entidade = entidade;
        this.detalhes = detalhes;
    }

    // Getters e Setters (pode gerar pelo IntelliJ)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getDataHora() { return dataHora; }
    public void setDataHora(Date dataHora) { this.dataHora = dataHora; }
    public String getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(String usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }
}
