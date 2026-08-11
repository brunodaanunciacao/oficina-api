package br.com.fiap.oficina.infrastructure.repositories;

import br.com.fiap.oficina.domain.peca.Peca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaRepository extends JpaRepository<Peca, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);
}