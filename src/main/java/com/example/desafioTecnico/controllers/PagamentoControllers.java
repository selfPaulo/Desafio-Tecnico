package com.example.desafioTecnico.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.desafioTecnico.entities.Pagamento;
import com.example.desafioTecnico.repositories.PagamentoRepository;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoControllers {

    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @GetMapping
    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }
    
    @GetMapping("/filtrar")
    public List<Pagamento> filtrarPagamentos(
            @RequestParam(required = false) Integer codigoDebito,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) String status) {
        if (codigoDebito != null) {
            return pagamentoRepository.findByCodigoDebito(codigoDebito);
        } else if (cpfCnpj != null) {
            return pagamentoRepository.findByCpfCnpj(cpfCnpj);
        } else if (status != null) {
            return pagamentoRepository.findByStatus(status);
        } else {
            return pagamentoRepository.findAll();
        }
    }
    
    @PostMapping
    public Pagamento receberPagamento(@RequestBody Pagamento pagamento) {
    	if (pagamento.getMetodoPagamento().equals("cartao_credito") || pagamento.getMetodoPagamento().equals("cartao_debito")) {
    	    if (pagamento.getNumeroCartao() == null || pagamento.getNumeroCartao().isEmpty()) {
    	        throw new RuntimeException("O campo número do cartão é obrigatório");
    	    }
    	}
        pagamento.setStatus("Pendente de Processamento");
        return pagamentoRepository.save(pagamento);
    }
    
    @PutMapping("/{id}/status")
    public Pagamento atualizarStatusPagamento(@PathVariable Long id, @RequestBody String status) {
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow();
        if (pagamento.getStatus().equals("Pendente de Processamento")) {
            pagamento.setStatus(status);
        } else if (pagamento.getStatus().equals("Processado com Falha") && status.equals("Pendente de Processamento")) {
            pagamento.setStatus(status);
        }
        return pagamentoRepository.save(pagamento);
    }
    
    @DeleteMapping("/{id}")
    public void excluirPagamento(@PathVariable Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow();
        if (pagamento.getStatus().equals("Pendente de Processamento")) {
            pagamento.setStatus("Inativo");
            pagamentoRepository.save(pagamento);
        }
    }
    
}
