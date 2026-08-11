package br.com.fiap.oficina.infrastructure.repositories;

import br.com.fiap.oficina.domain.ordemservico.OrdemServicoPeca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoPecaRepository
        extends JpaRepository<OrdemServicoPeca, Long> {

    List<OrdemServicoPeca> findByOrdemServicoId(Long ordemServicoId);
}