package com.suplementos.erp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "vendas")
public class Venda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date dataVenda;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;

    @ElementCollection
    private List<String> produtosVendidos;

    private double valorTotal;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    public Venda() {}

    // Construtor completo e corrigido
    public Venda(int id, Date dataVenda, Usuario cliente, Usuario funcionario, List<Produto> produtos, double valorTotal, FormaPagamento formaPagamento) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.cliente = cliente;
        this.funcionario = funcionario;
        // AQUI ESTÁ A CORREÇÃO: Usamos um stream para converter a lista de Produtos em uma lista de nomes
        this.produtosVendidos = produtos.stream()
                .map(Produto::getNome)
                .collect(Collectors.toList());
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
    }

    // Outros getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuario funcionario) {
        this.funcionario = funcionario;
    }

    public List<String> getProdutosVendidos() {
        return produtosVendidos;
    }

    public void setProdutosVendidos(List<String> produtosVendidos) {
        this.produtosVendidos = produtosVendidos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}