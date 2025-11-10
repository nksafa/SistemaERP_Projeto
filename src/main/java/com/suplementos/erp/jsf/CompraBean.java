package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.AuditLogRepository; // IMPORT NOVO
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
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ManagedBean
@ViewScoped
public class CompraBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer fornecedorId;
    private int produtoSelecionadoId;
    private int quantidade = 1;
    private Fornecedor fornecedorAtual;
    private List<Produto> produtosDoFornecedor;

    private ProdutoRepository produtoRepository;
    private CompraRepository compraRepository;
    private EstoqueService estoqueService;
    private FornecedorRepository fornecedorRepository;
    private AuditLogRepository auditLogRepository; // Repositório de Logs

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.compraRepository = new CompraRepository();
        this.fornecedorRepository = new FornecedorRepository();
        this.estoqueService = new EstoqueService(produtoRepository);
        this.auditLogRepository = new AuditLogRepository(); // Inicializa
    }

    // Método auxiliar para pegar o usuário atual
    private Usuario getUsuarioLogado() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    }

    public void carregarDadosIniciais() {
        if (fornecedorId != null && fornecedorId > 0) {
            this.fornecedorAtual = fornecedorRepository.buscarPorId(fornecedorId);
            this.produtosDoFornecedor = produtoRepository.buscarPorFornecedor(fornecedorId);
        }
    }

    public double getTotalDaCompra() {
        if (produtoSelecionadoId > 0 && quantidade > 0 && produtosDoFornecedor != null) {
            return produtosDoFornecedor.stream()
                    .filter(p -> p.getId() == produtoSelecionadoId)
                    .findFirst()
                    .map(produto -> produto.getPrecoCusto() * quantidade)
                    .orElse(0.0);
        }
        return 0.0;
    }

    public void realizarCompra() {
        Produto produtoSelecionado = produtoRepository.buscarPorId(produtoSelecionadoId);

        if (produtoSelecionado != null && quantidade > 0) {
            double totalDaCompra = getTotalDaCompra();
            Date dataCorreta = Date.from(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());

            Compra novaCompra = new Compra(dataCorreta, quantidade, produtoSelecionado.getPrecoCusto(), fornecedorAtual, produtoSelecionado);
            compraRepository.salvar(novaCompra);
            estoqueService.atualizarEstoque(produtoSelecionado.getId(), quantidade);

            // --- LOG DE COMPRA ---
            String detalhes = "Comprou " + quantidade + "x '" + produtoSelecionado.getNome() +
                    "' de " + fornecedorAtual.getNome() + " por R$ " + String.format("%.2f", totalDaCompra);
            auditLogRepository.salvar(new AuditLog(getUsuarioLogado().getNome(), "COMPRA", "Estoque", detalhes));
            // ---------------------

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Compra de R$ " + String.format("%.2f", totalDaCompra) + " realizada."));

            this.quantidade = 1;
            this.produtoSelecionadoId = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Selecione um produto e uma quantidade válida."));
        }
    }

    // Getters e Setters (mantidos iguais)
    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
    public Fornecedor getFornecedorAtual() { return fornecedorAtual; }
    public List<Produto> getProdutosDoFornecedor() { return produtosDoFornecedor; }
    public int getProdutoSelecionadoId() { return produtoSelecionadoId; }
    public void setProdutoSelecionadoId(int produtoSelecionadoId) { this.produtoSelecionadoId = produtoSelecionadoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}