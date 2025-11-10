package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Fornecedor;
import com.suplementos.erp.repository.FornecedorRepository;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class FornecedorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Fornecedor fornecedor = new Fornecedor();
    private FornecedorRepository fornecedorRepository = new FornecedorRepository();
    private List<Fornecedor> listaFornecedores;

    @PostConstruct
    public void init() {
        this.fornecedorRepository = new FornecedorRepository();
        this.fornecedor = new Fornecedor();
        this.listaFornecedores = fornecedorRepository.buscarTodos();
    }

    public void salvar() {
        fornecedorRepository.salvar(fornecedor);
        this.fornecedor = new Fornecedor();
        this.listaFornecedores = fornecedorRepository.buscarTodos();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Fornecedor salvo."));
    }


    public void desativar(Fornecedor fornecedor) {
        fornecedor.setAtivo(false);
        fornecedorRepository.salvar(fornecedor);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Fornecedor desativado com sucesso."));
    }

    public void editar(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void remover(Fornecedor fornecedor) {
        fornecedorRepository.remover(fornecedor.getId());
        this.listaFornecedores = fornecedorRepository.buscarTodos();
    }

    public List<Fornecedor> getListaFornecedores() {
        if (listaFornecedores == null) {
            listaFornecedores = fornecedorRepository.buscarTodos();
        }
        return listaFornecedores;
    }

    // Getters e Setters
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
}