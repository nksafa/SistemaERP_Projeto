package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Categoria;
import com.suplementos.erp.repository.CategoriaRepository;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;


@ManagedBean
@ViewScoped
public class
CategoriaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Categoria categoria;
    private CategoriaRepository categoriaRepository;
    private List<Categoria> listaCategorias;

    @PostConstruct
    public void init() {
        this.categoriaRepository = new CategoriaRepository();
        this.categoria = new Categoria();
        carregarCategorias();
    }
    private void carregarCategorias() {
        this.listaCategorias = categoriaRepository.buscarTodos();
    }

    public void salvar() {
        categoriaRepository.salvar(categoria);
        this.categoria = new Categoria(); // Reseta o formulário
        carregarCategorias();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Categoria salva com sucesso."));
    }

    public void remover(Categoria categoria) {
        categoriaRepository.remover(categoria.getId());
        carregarCategorias(); // Recarrega a lista
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Categoria removida."));
    }

    public void editar(Categoria categoriaSelecionada) {
        // Coloca a categoria clicada na variável do formulário para edição
        this.categoria = categoriaSelecionada;
    }

    // Getters e Setters
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public List<Categoria> getListaCategorias() { return listaCategorias; }
    public void setListaCategorias(List<Categoria> listaCategorias) { this.listaCategorias = listaCategorias; }
}
