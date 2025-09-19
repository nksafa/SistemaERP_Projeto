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

    private Integer numeroParcelas; // Para guardar a parcela selecionada (1, 2, 3...)
    private List<Integer> parcelasDisponiveis; // Para popular o dropdown

    private ClienteRepository clienteRepository;
    private List<Cliente> clientesDisponiveis;
    private int clienteId; // Guarda o ID do cliente selecionado no dropdown

    // Metodo que é chamado após a criação do bean para inicializar as dependências
    @PostConstruct
    public void init() {
        this.produtoRepository = new ProdutoRepository();
        this.vendaRepository = new VendaRepository();

        // AQUI ESTÁ A CORREÇÃO:
        this.estoqueService = new EstoqueService(produtoRepository);
        this.vendasService = new VendasService(vendaRepository, estoqueService, produtoRepository);

        carregarProdutos(); // Carrega os produtos após a inicialização

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
        if (formaPagamento != FormaPagamento.CREDITO) { // <-- VERIFIQUE O NOME EXATO DO SEU ENUM
            this.numeroParcelas = null; // Reseta a seleção de parcelas
        }
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
            return null; // Retorna null para permanecer na mesma página e exibir a mensagem de erro.
        }

        if (carrinho == null || carrinho.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", "O carrinho está vazio. Adicione produtos para continuar."));
            return null; // Permanece na mesma página.
        }

        double valorTotal = getValorTotal();


// <-- INÍCIO DA NOVA LÓgica: Buscar o cliente selecionado
        Cliente clienteSelecionado = null;
        // O clienteId vem do dropdown. Se for > 0, significa que um cliente foi escolhido.
        if (clienteId > 0) {
            clienteSelecionado = clienteRepository.buscarPorId(clienteId);
        }


        Venda novaVenda = new Venda(0, new Date(), clienteSelecionado, usuarioLogado, carrinho, valorTotal, formaPagamento);

        vendasService.realizarVenda(novaVenda);

        System.out.println("Venda #" + novaVenda.getId() + " finalizada com " + formaPagamento + " em " + this.numeroParcelas + "x");

        carrinho.clear();
        this.formaPagamento = null;
        this.numeroParcelas = null; // Limpa o número de parcelas selecionado.
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

    // Adicione estes métodos na sua classe VendaBean.java

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