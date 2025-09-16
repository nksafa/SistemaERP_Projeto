package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.CategoriaRepository;
import com.suplementos.erp.repository.FornecedorRepository;
import com.suplementos.erp.repository.ProdutoRepository;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ProdutoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Produto produto; // Inicializa com o construtor vazio
    private int categoriaId; // <- Agora vamos usar o ID
    private int fornecedorId; // <- Agora vamos usar o ID

    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;
    private FornecedorRepository fornecedorRepository;

    private List<Categoria> categoriasDisponiveis;
    private List<Fornecedor> fornecedoresDisponiveis;

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.categoriaRepository = new CategoriaRepository();
        this.fornecedorRepository = new FornecedorRepository();
        this.produto = new Produto();
        this.categoriasDisponiveis = categoriaRepository.buscarTodos();
        this.fornecedoresDisponiveis = fornecedorRepository.buscarTodos();
    }

    public void salvar() {
        // AQUI ESTÁ A LÓGICA DE CONVERSÃO MANUAL, AGORA COM IDS
        Categoria categoria = categoriaRepository.buscarPorId(categoriaId);
        Fornecedor fornecedor = fornecedorRepository.buscarPorId(fornecedorId);

        if (categoria == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Categoria não encontrada."));
            return;
        }
        if (fornecedor == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Fornecedor não encontrado."));
            return;
        }

        this.produto.setCategoria(categoria);
        this.produto.setFornecedor(fornecedor);
        this.produtoRepository.salvar(this.produto);

        this.produto = new Produto(); // Reseta o formulário
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto salvo com sucesso."));
    }


    public String editar(Produto produto) {
        this.produto = produto;
        this.categoriaId = produto.getCategoria().getId();
        this.fornecedorId = produto.getFornecedor().getId();
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public void remover(Produto produto) {
        produtoRepository.remover(produto.getId());
        // Reseta o formulário após remover
        this.produto = new Produto();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto removido com sucesso."));
    }

    public String novoProduto() {
        this.produto = new Produto(); // Cria um novo e vazio objeto Produto
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public List<Produto> getListaProdutos() {
        return produtoRepository.buscarTodos();
    }

    // Getters e Setters

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public int getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(int fornecedorId) { this.fornecedorId = fornecedorId; }

    public List<Categoria> getCategoriasDisponiveis() { return categoriasDisponiveis; }
    public List<Fornecedor> getFornecedoresDisponiveis() { return fornecedoresDisponiveis; }

}