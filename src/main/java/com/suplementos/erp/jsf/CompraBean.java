package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Compra;
import com.suplementos.erp.model.Fornecedor;
import com.suplementos.erp.model.Produto;
import com.suplementos.erp.repository.CompraRepository;
import com.suplementos.erp.repository.FornecedorRepository;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.service.EstoqueService;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class CompraBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Fornecedor fornecedorAtual;
    private int produtoSelecionadoId;
    private int quantidade;
    private Integer fornecedorId; // AQUI ESTÁ A CORREÇÃO

    private ProdutoRepository produtoRepository;
    private CompraRepository compraRepository;
    private EstoqueService estoqueService;
    private FornecedorRepository fornecedorRepository;

    private List<Produto> produtosDoFornecedor;
    private List<Compra> historicoCompras;
    private List<Fornecedor> fornecedoresDisponiveis;
    private double totalDaCompra;

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.compraRepository = new CompraRepository();
        this.fornecedorRepository = new FornecedorRepository();
        this.estoqueService = new EstoqueService(produtoRepository);
        this.fornecedoresDisponiveis = fornecedorRepository.buscarTodos();
    }

    // Dentro da classe CompraBean.java
// Este método agora é chamado na navegação
    public void carregarProdutos() {
        if (fornecedorId != null) {
            this.fornecedorAtual = fornecedorRepository.buscarPorId(fornecedorId);
            this.produtosDoFornecedor = produtoRepository.buscarPorFornecedor(fornecedorId);
        }
    }

    public void realizarCompra() {
        Produto produtoSelecionado = produtoRepository.buscarPorId(produtoSelecionadoId);
        if (produtoSelecionado != null && quantidade > 0) {
            // Adiciona a lógica para calcular e exibir o valor total
            this.totalDaCompra = produtoSelecionado.getPrecoCusto() * quantidade;

            Compra novaCompra = new Compra(new java.util.Date(), quantidade, produtoSelecionado.getPrecoCusto(), fornecedorAtual, produtoSelecionado);
            compraRepository.salvar(novaCompra);

            // Atualiza o estoque
            estoqueService.atualizarEstoque(produtoSelecionado.getId(), quantidade);

            // Exibe uma mensagem de sucesso
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra realizada com sucesso!", null));

            // Reseta o formulário
            this.quantidade = 0;
            this.produtoSelecionadoId = 0;
        }
    }

    public void calcularTotal() {
        // Este método será chamado pelo AJAX
        Produto produtoSelecionado = produtoRepository.buscarPorId(produtoSelecionadoId);
        if (produtoSelecionado != null) {
            this.totalDaCompra = produtoSelecionado.getPrecoCusto() * quantidade;
        }
    }
    public double getPrecoCustoProdutoSelecionado() {
        Produto produto = produtoRepository.buscarPorId(produtoSelecionadoId);
        if (produto != null) {
            return produto.getPrecoCusto();
        }
        return 0.0;
    }

    // Getters e Setters
    public Fornecedor getFornecedorAtual() { return fornecedorAtual; }
    public void setFornecedorAtual(Fornecedor fornecedorAtual) { this.fornecedorAtual = fornecedorAtual; }
    public int getProdutoSelecionadoId() { return produtoSelecionadoId; }
    public void setProdutoSelecionadoId(int produtoSelecionadoId) { this.produtoSelecionadoId = produtoSelecionadoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public List<Produto> getProdutosDoFornecedor() { return produtosDoFornecedor; }
    public List<Compra> getHistoricoCompras() { return historicoCompras; }
    public void setHistoricoCompras(List<Compra> historicoCompras) { this.historicoCompras = historicoCompras; }
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    public List<Fornecedor> getFornecedoresDisponiveis() { return fornecedoresDisponiveis; }
    public void setFornecedoresDisponiveis(List<Fornecedor> fornecedoresDisponiveis) { this.fornecedoresDisponiveis = fornecedoresDisponiveis; }
    public double getTotalDaCompra() {
        return totalDaCompra;
    }
    public void setTotalDaCompra(double totalDaCompra) {
        this.totalDaCompra = totalDaCompra;
    }
}