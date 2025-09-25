package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.repository.ClienteRepository;
import com.suplementos.erp.service.EstoqueService;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.VendaRepository;
import com.suplementos.erp.service.VendasService;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.ZoneId;
import java.time.ZonedDateTime;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class VendaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoRepository produtoRepository;
    private VendaRepository vendaRepository;
    private EstoqueService estoqueService;
    private VendasService vendasService;

    private List<Produto> produtosDisponiveis;
    private List<Produto> carrinho = new ArrayList<>();
    private FormaPagamento formaPagamento;

    private Integer numeroParcelas;
    private List<Integer> parcelasDisponiveis;

    private ClienteRepository clienteRepository;
    private List<Cliente> clientesDisponiveis;
    private int clienteId;

    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.vendaRepository = new VendaRepository();

        this.estoqueService = new EstoqueService(produtoRepository);
        this.vendasService = new VendasService(vendaRepository, estoqueService, produtoRepository);

        carregarProdutos();

        parcelasDisponiveis = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            parcelasDisponiveis.add(i);

        }
        this.clienteRepository = new ClienteRepository();
        this.clientesDisponiveis = clienteRepository.buscarTodos();
    }

    public String irParaCadastroCliente() {
        return "gerenciar-clientes.xhtml?faces-redirect=true";
    }

    public void onFormaPagamentoChange() {
        if (formaPagamento != FormaPagamento.CREDITO) {
            this.numeroParcelas = null;
        }
    }

    public void carregarProdutos() {
        this.produtosDisponiveis = produtoRepository.buscarTodos();
    }

    public void adicionarAoCarrinho(Produto produto) {
        carrinho.add(produto);
        System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho.");
    }

    public void removerDoCarrinho(Produto produto) {
        carrinho.remove(produto);
    }

    public double getValorTotal() {
        return carrinho.stream().mapToDouble(Produto::getPreco).sum();
    }

    public FormaPagamento[] getFormasPagamento() {
        return FormaPagamento.values();
    }

    public String finalizarVenda() {

       Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");

        if (usuarioLogado == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Sessão expirada. Por favor, faça o login novamente."));
            return "login?faces-redirect=true";
        }

        if (formaPagamento == FormaPagamento.CREDITO && (numeroParcelas == null || numeroParcelas <= 0)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", "Por favor, selecione o número de parcelas."));
            return null;
        }

        if (carrinho == null || carrinho.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", "O carrinho está vazio. Adicione produtos para continuar."));
            return null;
        }

        double valorTotal = getValorTotal();


        Cliente clienteSelecionado = null;
        if (clienteId > 0) {
            clienteSelecionado = clienteRepository.buscarPorId(clienteId);
        }


        Date dataCorreta = Date.from(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());

        Venda novaVenda = new Venda(0, dataCorreta, clienteSelecionado, usuarioLogado, carrinho, valorTotal, formaPagamento, this.numeroParcelas);        vendasService.realizarVenda(novaVenda);

        System.out.println("Venda #" + novaVenda.getId() + " finalizada com " + formaPagamento + " em " + this.numeroParcelas + "x");

        carrinho.clear();
        this.formaPagamento = null;
        this.numeroParcelas = null;
        this.clienteId = 0;

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Venda realizada com sucesso."));

         return "gerenciar-vendas.xhtml?faces-redirect=true";
    }


    // Getters e Setters
    public Integer getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(Integer numeroParcelas) { this.numeroParcelas = numeroParcelas; }
    public List<Integer> getParcelasDisponiveis() { return parcelasDisponiveis; }


    public List<Cliente> getClientesDisponiveis() {
        return clientesDisponiveis;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public List<Produto> getProdutosDisponiveis() { return produtosDisponiveis; }
    public void setProdutosDisponiveis(List<Produto> produtosDisponiveis) { this.produtosDisponiveis = produtosDisponiveis; }
    public List<Produto> getCarrinho() { return carrinho; }
    public void setCarrinho(List<Produto> carrinho) { this.carrinho = carrinho; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
}