package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Cliente;
import com.suplementos.erp.repository.ClienteRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cliente cliente;
    private List<Cliente> listaClientes;
    private ClienteRepository clienteRepository;

    @PostConstruct
    public void init() {
        clienteRepository = new ClienteRepository();
        cliente = new Cliente();
        listaClientes = clienteRepository.buscarTodos();
    }

    public void salvar() {
        clienteRepository.salvar(cliente);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Cliente salvo com sucesso."));
        cliente = new Cliente();
        listaClientes = clienteRepository.buscarTodos();
    }

    public void editar(Cliente clienteSelecionado) {
        this.cliente = clienteSelecionado;
    }

    public void remover(Cliente clienteParaRemover) {
        clienteRepository.remover(clienteParaRemover);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Cliente removido."));
        listaClientes = clienteRepository.buscarTodos();
    }

    // Getters e Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }
}