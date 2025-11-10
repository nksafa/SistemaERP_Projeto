package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Venda;
import com.suplementos.erp.repository.VendaRepository;
import com.suplementos.erp.service.VendasService;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class HistoricoVendasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final VendasService vendasService;
    private List<Venda> historicoVendas;
    private List<Venda> historicoFiltrado;


    // Inicialização do Bean
    public HistoricoVendasBean() {
        // Instancia as dependências
        VendaRepository vendaRepository = new VendaRepository();
        this.vendasService = new VendasService(vendaRepository, null, null);
    }

    @PostConstruct
    public void init() {
        this.historicoVendas = vendasService.listarHistoricoVendas();
    }

    public List<Venda> getHistoricoVendas() {
        return historicoVendas;
    }

    public void setHistoricoVendas(List<Venda> historicoVendas) {
        this.historicoVendas = historicoVendas;
    }

    public void removerVenda(Venda vendaParaRemover) {

        vendasService.removerVenda(vendaParaRemover.getId());

        this.historicoVendas = vendasService.listarHistoricoVendas(); // Recarrega a lista

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Venda #" + vendaParaRemover.getId() + " removida com sucesso."));
    }

    public List<Venda> getHistoricoFiltrado() {
        return historicoFiltrado;
    }
    public void setHistoricoFiltrado(List<Venda> historicoFiltrado) {
        this.historicoFiltrado = historicoFiltrado;
    }
}