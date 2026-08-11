package br.com.fiap.oficina.infrastructure.repositories;

import br.com.fiap.oficina.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}