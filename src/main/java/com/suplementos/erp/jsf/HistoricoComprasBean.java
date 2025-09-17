package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Compra;
import com.suplementos.erp.repository.CompraRepository;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class HistoricoComprasBean implements Serializable {

    private CompraRepository compraRepository;
    private List<Compra> historicoCompras;

    @PostConstruct
    public void init() {
        this.compraRepository = new CompraRepository();
        this.historicoCompras = compraRepository.buscarTodos();
    }

    // Getters e Setters
    public List<Compra> getHistoricoCompras() {
        return historicoCompras;
    }

    public void setHistoricoCompras(List<Compra> historicoCompras) {
        this.historicoCompras = historicoCompras;
    }
}