package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Compra;
import com.suplementos.erp.model.Fornecedor;
import com.suplementos.erp.model.Produto;
import com.suplementos.erp.repository.CompraRepository;
import com.suplementos.erp.repository.FornecedorRepository;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.service.EstoqueService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class CompraBean implements Serializable {

    private Fornecedor fornecedorAtual;
    private Produto produtoSelecionado;
    private int quantidade;

    private ProdutoRepository produtoRepository;
    private CompraRepository compraRepository;
    private EstoqueService estoqueService;
    private FornecedorRepository fornecedorRepository;

    private List<Produto> produtosDoFornecedor;
    private List<Compra> historicoCompras;

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.compraRepository = new CompraRepository();
        // AQUI ESTÁ A CORREÇÃO:
        // A inicialização do fornecedorRepository estava faltando
        this.fornecedorRepository = new FornecedorRepository();
        this.estoqueService = new EstoqueService(produtoRepository);
    }

// Dentro da classe CompraBean.java

    public String carregarProdutos(Integer fornecedorId) {
        this.fornecedorAtual = fornecedorRepository.buscarPorId(fornecedorId);
        if (this.fornecedorAtual != null) {
            this.produtosDoFornecedor = produtoRepository.buscarPorFornecedor(fornecedorId);
        }
        return "realizar-compra.xhtml?faces-redirect=true";
    }

    public void realizarCompra() {
        if (produtoSelecionado != null && quantidade > 0) {
            // Lógica de Compra
            Compra novaCompra = new Compra(new java.util.Date(), quantidade, produtoSelecionado.getPrecoCusto(), fornecedorAtual, produtoSelecionado);
            compraRepository.salvar(novaCompra);

            // Atualiza o estoque
            estoqueService.atualizarEstoque(produtoSelecionado.getId(), quantidade);

            // Reseta o formulário
            this.quantidade = 0;
            this.produtoSelecionado = null;
        }
    }

    // Getters e Setters
    public Fornecedor getFornecedorAtual() { return fornecedorAtual; }
    public void setFornecedorAtual(Fornecedor fornecedorAtual) { this.fornecedorAtual = fornecedorAtual; }
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public List<Produto> getProdutosDoFornecedor() { return produtosDoFornecedor; }
}