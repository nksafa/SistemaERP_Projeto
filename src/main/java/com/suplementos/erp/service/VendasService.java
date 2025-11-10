package com.suplementos.erp.service;

import com.suplementos.erp.model.Produto;
import com.suplementos.erp.model.Venda;
import com.suplementos.erp.service.EstoqueService;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.VendaRepository;

import java.util.List;

public class VendasService {

    private final VendaRepository vendaRepository;
    private final EstoqueService estoqueService;
    private final ProdutoRepository produtoRepository;

    public VendasService(VendaRepository vendaRepository, EstoqueService estoqueService, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.estoqueService = estoqueService;
        this.produtoRepository = produtoRepository;
    }

    public void realizarVenda(Venda novaVenda) {
        vendaRepository.salvar(novaVenda);


        for (String nomeProduto : novaVenda.getProdutosVendidos()) {
            // Buscamos o produto completo no banco de dados para obter o ID
            Produto produto = produtoRepository.buscarPorNome(nomeProduto);
            if (produto != null) {
                estoqueService.atualizarEstoque(produto.getId(), -1);
            }
        }

        System.out.println("Venda de R$" + novaVenda.getValorTotal() + " finalizada com sucesso!");
    }

    public List<Venda> listarHistoricoVendas() {
        return vendaRepository.buscarTodos();
    }
    public void removerVenda(int idVenda) {
        vendaRepository.remover(idVenda);
        System.out.println("Venda #" + idVenda + " removida com sucesso.");
    }
}