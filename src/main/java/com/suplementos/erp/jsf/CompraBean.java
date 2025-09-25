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
import java.util.Date; // Import para java.util.Date
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ManagedBean
@ViewScoped
public class CompraBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos para os dados da tela
    private Integer fornecedorId;
    private int produtoSelecionadoId;
    private int quantidade = 1;

    // Atributos para carregar informações
    private Fornecedor fornecedorAtual;
    private List<Produto> produtosDoFornecedor;

    // Repositórios e Serviços
    private ProdutoRepository produtoRepository;
    private CompraRepository compraRepository;
    private EstoqueService estoqueService;
    private FornecedorRepository fornecedorRepository;

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.compraRepository = new CompraRepository();
        this.fornecedorRepository = new FornecedorRepository();
        this.estoqueService = new EstoqueService(produtoRepository);
    }

    // Mtodo chamado pela página para carregar os dados iniciais
    public void carregarDadosIniciais() {
        if (fornecedorId != null && fornecedorId > 0) {
            this.fornecedorAtual = fornecedorRepository.buscarPorId(fornecedorId);
            this.produtosDoFornecedor = produtoRepository.buscarPorFornecedor(fornecedorId);
        }
    }

    // Getter que calcula o total sob demanda
    public double getTotalDaCompra() {
        if (produtoSelecionadoId > 0 && quantidade > 0 && produtosDoFornecedor != null) {
            // Encontra o produto selecionado na lista para pegar seu preço de custo
            return produtosDoFornecedor.stream()
                    .filter(p -> p.getId() == produtoSelecionadoId)
                    .findFirst()
                    .map(produto -> produto.getPrecoCusto() * quantidade) // Calcula o total
                    .orElse(0.0); // Retorna 0.0 se não encontrar o produto
        }
        return 0.0;
    }

    public void realizarCompra() {
        Produto produtoSelecionado = produtoRepository.buscarPorId(produtoSelecionadoId);

        if (produtoSelecionado != null && quantidade > 0) {
            double totalDaCompra = getTotalDaCompra(); // Usa o getter para pegar o total calculado


            Date dataCorreta = Date.from(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());

            Compra novaCompra = new Compra(dataCorreta, quantidade, produtoSelecionado.getPrecoCusto(), fornecedorAtual, produtoSelecionado);            compraRepository.salvar(novaCompra);

            // Atualiza o estoque
            estoqueService.atualizarEstoque(produtoSelecionado.getId(), quantidade);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Compra de R$ " + String.format("%.2f", totalDaCompra) + " realizada."));

            // Reseta o formulário
            this.quantidade = 1;
            this.produtoSelecionadoId = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Selecione um produto e uma quantidade válida."));
        }
    }

    // Getters e Setters necessários para a página
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    public Fornecedor getFornecedorAtual() { return fornecedorAtual; }
    public List<Produto> getProdutosDoFornecedor() { return produtosDoFornecedor; }
    public int getProdutoSelecionadoId() { return produtoSelecionadoId; }
    public void setProdutoSelecionadoId(int produtoSelecionadoId) { this.produtoSelecionadoId = produtoSelecionadoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}