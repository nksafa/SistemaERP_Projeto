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

    private final VendasService vendasService;
    private List<Venda> historicoVendas;

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
    public void removerVenda(int idVenda) {
        vendasService.removerVenda(idVenda);
        this.historicoVendas = vendasService.listarHistoricoVendas(); // Recarrega a lista

        // Adiciona uma mensagem de sucesso
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Venda removida com sucesso."));
    }
}