package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Categoria;
import com.suplementos.erp.model.Fornecedor;
import com.suplementos.erp.model.Produto;
import com.suplementos.erp.repository.CategoriaRepository;
import com.suplementos.erp.repository.FornecedorRepository;
import com.suplementos.erp.repository.ProdutoRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ProdutoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;
    private FornecedorRepository fornecedorRepository;

    private Produto produto;
    private int categoriaId;
    private int fornecedorId;
    private List<Categoria> categoriasDisponiveis;
    private List<Fornecedor> fornecedoresDisponiveis;

    private List<Produto> listaProdutos;
    private List<Produto> listaProdutosFiltrada; // Essencial para o filtro do PrimeFaces

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.categoriaRepository = new CategoriaRepository();
        this.fornecedorRepository = new FornecedorRepository();

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        if (flash.containsKey("produto")) {
            this.produto = (Produto) flash.get("produto");
            if (this.produto.getCategoria() != null) {
                this.categoriaId = this.produto.getCategoria().getId();
            }
            if (this.produto.getFornecedor() != null) {
                this.fornecedorId = this.produto.getFornecedor().getId();
            }
        } else {
            this.produto = new Produto();
        }

        // Carrega as listas necessárias
        carregarProdutos();
        this.categoriasDisponiveis = categoriaRepository.buscarTodos();
        this.fornecedoresDisponiveis = fornecedorRepository.buscarTodos();
    }

    private void carregarProdutos() {
        this.listaProdutos = produtoRepository.buscarTodos();
    }

    public void salvar() {
        try {
            Categoria categoria = categoriaRepository.buscarPorId(categoriaId);
            Fornecedor fornecedor = fornecedorRepository.buscarPorId(fornecedorId);
            produto.setCategoria(categoria);
            produto.setFornecedor(fornecedor);

            produtoRepository.salvar(produto);
            this.produto = new Produto();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto salvo."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao Salvar", "Ocorreu um erro ao salvar o produto."));
        }
    }


    public String editar(Produto produtoParaEditar) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("produto", produtoParaEditar);
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public String novoProduto() {
        return "cadastro-produtos.xhtml?faces-redirect=true";
    }

    public void desativar(Produto produtoParaDesativar) {
        produtoParaDesativar.setAtivo(false); // Exemplo de lógica para desativar
        produtoRepository.salvar(produtoParaDesativar);

        carregarProdutos();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso", produtoParaDesativar.getNome() + " foi desativado."));
    }


    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public int getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(int fornecedorId) { this.fornecedorId = fornecedorId; }
    public List<Categoria> getCategoriasDisponiveis() { return categoriasDisponiveis; }
    public List<Fornecedor> getFornecedoresDisponiveis() { return fornecedoresDisponiveis; }
    public List<Produto> getListaProdutos() { return listaProdutos; }
    public void setListaProdutos(List<Produto> listaProdutos) { this.listaProdutos = listaProdutos; }
    public List<Produto> getListaProdutosFiltrada() { return listaProdutosFiltrada; }
    public void setListaProdutosFiltrada(List<Produto> listaProdutosFiltrada) { this.listaProdutosFiltrada = listaProdutosFiltrada; }
}