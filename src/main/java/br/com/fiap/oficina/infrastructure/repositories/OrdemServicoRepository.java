package br.com.fiap.oficina.infrastructure.repositories;

import br.com.fiap.oficina.domain.ordemservico.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoRepository
        extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByVeiculoId(Long veiculoId);
}