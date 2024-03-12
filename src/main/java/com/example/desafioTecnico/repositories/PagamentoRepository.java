package com.example.desafioTecnico.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.desafioTecnico.entities.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByCodigoDebito(Integer codigoDebito);

    List<Pagamento> findByCpfCnpj(String cpfCnpjPagador);

    List<Pagamento> findByStatus(String status);

}

