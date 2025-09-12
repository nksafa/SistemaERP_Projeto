package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
import com.suplementos.erp.service.EstoqueService;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.VendaRepository;
import com.suplementos.erp.service.VendasService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class VendaBean implements Serializable {

    private ProdutoRepository produtoRepository;
    private VendaRepository vendaRepository;
    private EstoqueService estoqueService;
    private VendasService vendasService;

    private List<Produto> produtosDisponiveis;
    private List<Produto> carrinho = new ArrayList<>();
    private FormaPagamento formaPagamento;

    // Metodo que é chamado após a criação do bean para inicializar as dependências
    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.vendaRepository = new VendaRepository();

        // AQUI ESTÁ A CORREÇÃO:
        this.estoqueService = new EstoqueService(produtoRepository);
        this.vendasService = new VendasService(vendaRepository, estoqueService, produtoRepository);

        carregarProdutos(); // Carrega os produtos após a inicialização
    }

    // Metodo para carregar os produtos disponíveis no banco de dados
    public void carregarProdutos() {
        this.produtosDisponiveis = produtoRepository.buscarTodos();
    }

    // Metodo para adicionar um produto ao carrinho
    public void adicionarAoCarrinho(Produto produto) {
        carrinho.add(produto);
        System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho.");
    }

    // Metodo para remover um produto do carrinho
    public void removerDoCarrinho(Produto produto) {
        carrinho.remove(produto);
    }

    // Metodo para calcular o valor total
    public double getValorTotal() {
        return carrinho.stream().mapToDouble(Produto::getPreco).sum();
    }

    // Enum para as formas de pagamento para o xhtml
    public FormaPagamento[] getFormasPagamento() {
        return FormaPagamento.values();
    }

    // Metodo que finaliza a venda
    public String finalizarVenda() {
        Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        if (usuarioLogado == null) {
            return "login?faces-redirect=true";
        }

        // CÁLCULO DO VALOR TOTAL
        double valorTotal = getValorTotal();

        // Lógica de venda
        Venda novaVenda = new Venda(0, new Date(), null, usuarioLogado, carrinho, valorTotal, formaPagamento);
        vendasService.realizarVenda(novaVenda);

        // Limpa o carrinho e redireciona
        carrinho.clear();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Venda Realizada!", "A venda foi concluída com sucesso."));
        return "dashboard?faces-redirect=true";
    }

    // Getters e Setters
    public List<Produto> getProdutosDisponiveis() { return produtosDisponiveis; }
    public void setProdutosDisponiveis(List<Produto> produtosDisponiveis) { this.produtosDisponiveis = produtosDisponiveis; }
    public List<Produto> getCarrinho() { return carrinho; }
    public void setCarrinho(List<Produto> carrinho) { this.carrinho = carrinho; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
}