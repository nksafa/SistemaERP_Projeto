package com.suplementos.erp.jsf;




import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean // Diz ao JSF que esta é uma classe de back-end
@SessionScoped // Define o escopo da classe
public class IndexBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mensagem = "Bem-vindo ao sistema ERP da sua loja de suplementos!";

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}