package com.suplementos.erp.jsf;

import com.suplementos.erp.model.*;
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

    // Metodo que finaliza a venda
    public String finalizarVenda() {

        // 1. VERIFICAÇÃO DO USUÁRIO LOGADO
        // Pega o usuário da sessão para associá-lo à venda.
        Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");

        // Se não houver usuário logado, redireciona para a página de login.
        if (usuarioLogado == null) {
            // Adiciona uma mensagem de erro para o usuário.
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Sessão expirada. Por favor, faça o login novamente."));
            return "login?faces-redirect=true";
        }

        // 2. VALIDAÇÃO DAS PARCELAS (LÓGICA NOVA)
        // Se a forma de pagamento for cartão de crédito, o número de parcelas é obrigatório.
        if (formaPagamento == FormaPagamento.CREDITO && (numeroParcelas == null || numeroParcelas <= 0)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", "Por favor, selecione o número de parcelas."));
            return null; // Retorna null para permanecer na mesma página e exibir a mensagem de erro.
        }

        // 3. VALIDAÇÃO DO CARRINHO
        // Impede a finalização de uma venda com o carrinho vazio.
        if (carrinho == null || carrinho.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Validação", "O carrinho está vazio. Adicione produtos para continuar."));
            return null; // Permanece na mesma página.
        }

        // 4. CRIAÇÃO E PERSISTÊNCIA DA VENDA
        // Calcula o valor total a partir dos itens do carrinho.
        double valorTotal = getValorTotal();

        // Cria um novo objeto Venda com todos os dados necessários.
        // Lembre-se que você precisará adicionar o campo 'numeroParcelas' na sua classe Venda.
        Venda novaVenda = new Venda(0, new Date(), null, usuarioLogado, carrinho, valorTotal, formaPagamento);
        // vendasService.setNumeroParcelas(this.numeroParcelas); // Exemplo de como você poderia passar as parcelas para o service

        // Chama o serviço que contém a regra de negócio para realizar a venda.
        vendasService.realizarVenda(novaVenda);

        // Apenas para depuração, você pode imprimir os dados no console.
        System.out.println("Venda #" + novaVenda.getId() + " finalizada com " + formaPagamento + " em " + this.numeroParcelas + "x");

        // 5. LIMPEZA E FEEDBACK
        // Limpa o estado do bean para preparar para a próxima venda.
        carrinho.clear();
        this.formaPagamento = null;
        this.numeroParcelas = null; // Limpa o número de parcelas selecionado.

        // Adiciona uma mensagem de sucesso que será exibida na próxima página (após o redirect).
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Venda realizada com sucesso."));

        // 6. REDIRECIONAMENTO
        // Redireciona o usuário para a página de gerenciamento de vendas.
        return "gerenciar-vendas.xhtml?faces-redirect=true";
    }


    // Getters e Setters
    public Integer getNumeroParcelas() { return numeroParcelas; }
    public void setNumeroParcelas(Integer numeroParcelas) { this.numeroParcelas = numeroParcelas; }
    public List<Integer> getParcelasDisponiveis() { return parcelasDisponiveis; }

    public List<Produto> getProdutosDisponiveis() { return produtosDisponiveis; }
    public void setProdutosDisponiveis(List<Produto> produtosDisponiveis) { this.produtosDisponiveis = produtosDisponiveis; }
    public List<Produto> getCarrinho() { return carrinho; }
    public void setCarrinho(List<Produto> carrinho) { this.carrinho = carrinho; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
}