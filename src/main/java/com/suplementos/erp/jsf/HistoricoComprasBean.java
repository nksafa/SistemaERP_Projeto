package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Compra;
import com.suplementos.erp.repository.CompraRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;


@ManagedBean
@ViewScoped
public class HistoricoComprasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompraRepository compraRepository = new CompraRepository();
    private List<Compra> historicoCompras;
    private List<Compra> historicoFiltrado;

    @PostConstruct
    public void init() {
        this.historicoCompras = compraRepository.buscarTodos();
    }


    public void remover(Compra compra) {
        compraRepository.remover(compra.getId());
        this.historicoCompras = compraRepository.buscarTodos(); // Recarrega a lista
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Compra removida com sucesso."));
    }

    // Getters e Setters
    public List<Compra> getHistoricoCompras() {
        return historicoCompras;
    }
    public void setHistoricoCompras(List<Compra> historicoCompras) {
        this.historicoCompras = historicoCompras;
    }
    public List<Compra> getHistoricoFiltrado() {
        return historicoFiltrado;
    }
    public void setHistoricoFiltrado(List<Compra> historicoFiltrado) {
        this.historicoFiltrado = historicoFiltrado;
    }
}