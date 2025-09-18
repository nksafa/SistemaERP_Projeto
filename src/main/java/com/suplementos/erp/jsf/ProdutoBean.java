package com.suplementos.erp.jsf;
import com.suplementos.erp.model.*;
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


        private Produto produto;
        private int categoriaId;
        private int fornecedorId;

        private ProdutoRepository produtoRepository;
        private CategoriaRepository categoriaRepository;
        private FornecedorRepository fornecedorRepository;

        private List<Categoria> categoriasDisponiveis;
        private List<Fornecedor> fornecedoresDisponiveis;

        @PostConstruct
        public void init() {
            // Tenta pegar o objeto "produto" que foi guardado no Flash.
            this.produto = (Produto) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("produto");

            this.produtoRepository = new ProdutoRepository();
            this.categoriaRepository = new CategoriaRepository();
            this.fornecedorRepository = new FornecedorRepository();

            // Se não veio nada do Flash (ou seja, não é uma edição), então cria um produto novo.
            if (this.produto == null) {
                this.produto = new Produto();
            }

            // O resto do seu código continua perfeito
            this.categoriasDisponiveis = categoriaRepository.buscarTodos();
            this.fornecedoresDisponiveis = fornecedorRepository.buscarTodos();

            if (this.produto.getCategoria() != null) {
                this.categoriaId = this.produto.getCategoria().getId();
            }
            if (this.produto.getFornecedor() != null) {
                this.fornecedorId = this.produto.getFornecedor().getId();
            }
        }

        // Dentro da classe ProdutoBean.java

        public void salvar() {
            // AQUI ESTÁ A LÓGICA DE SALVAMENTO CORRIGIDA
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

            // AQUI ESTÁ A CORREÇÃO:
            // O JSF já preencheu o objeto 'produto'. Apenas o preparamos para o Hibernate.
            this.produto.setCategoria(categoria);
            this.produto.setFornecedor(fornecedor);

            this.produtoRepository.salvar(this.produto);

            this.produto = new Produto(); // Reseta o formulário
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto salvo com sucesso."));
        }

        // Dentro da classe ProdutoBean.java

        public void desativar(Produto produto) {
            produto.setAtivo(false);
            produtoRepository.salvar(produto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto desativado com sucesso."));
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

        public List<Produto> getListaProdutos() { return produtoRepository.buscarTodos(); }

        public String editar(Produto produto) {
            // 1. Pega o "Flash" da requisição atual.
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

            // 2. Coloca o objeto que você quer passar para a próxima página no Flash.
            // A chave "produto" é um nome que você escolhe.
            flash.put("produto", produto);

            // 3. O redirect continua o mesmo.
            return "cadastro-produtos.xhtml?faces-redirect=true";
        }

        public void remover(Produto produto) {
            produtoRepository.remover(produto.getId());
            this.produto = new Produto();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Produto removido com sucesso."));
        }
        public String novoProduto() {
            this.produto = new Produto();
            return "cadastro-produtos.xhtml?faces-redirect=true";
        }
    }