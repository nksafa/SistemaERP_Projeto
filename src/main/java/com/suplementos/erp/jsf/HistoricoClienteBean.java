package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Cliente;
import com.suplementos.erp.model.Venda;
import com.suplementos.erp.repository.ClienteRepository;
import com.suplementos.erp.repository.VendaRepository;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class HistoricoClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int clienteId;
    private Cliente cliente;
    private List<Venda> historicoDeVendas;

    private ClienteRepository clienteRepository;
    private VendaRepository vendaRepository;

    @PostConstruct
    public void init() {
        this.clienteRepository = new ClienteRepository();
        this.vendaRepository = new VendaRepository();
    }

    public void carregarDados() {
        if (clienteId > 0) {
            this.cliente = clienteRepository.buscarPorId(clienteId);
            this.historicoDeVendas = vendaRepository.buscarPorClienteId(clienteId);
        }
    }

    // Getters e Setters
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Venda> getHistoricoDeVendas() {
        return historicoDeVendas;
    }
}