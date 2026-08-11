package br.com.fiap.oficina.infrastructure.repositories;

import br.com.fiap.oficina.domain.ordemservico.OrdemServicoServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoServicoRepository
        extends JpaRepository<OrdemServicoServico, Long> {

    List<OrdemServicoServico> findByOrdemServicoId(Long ordemServicoId);
}