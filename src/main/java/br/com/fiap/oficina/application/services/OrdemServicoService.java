package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.ordemservico.OrdemServico;
import br.com.fiap.oficina.domain.ordemservico.OrdemServicoPeca;
import br.com.fiap.oficina.domain.ordemservico.OrdemServicoServico;
import br.com.fiap.oficina.domain.ordemservico.StatusOrdemServico;
import br.com.fiap.oficina.domain.peca.Peca;
import br.com.fiap.oficina.domain.servico.Servico;
import br.com.fiap.oficina.domain.veiculo.Veiculo;
import br.com.fiap.oficina.infrastructure.repositories.OrdemServicoPecaRepository;
import br.com.fiap.oficina.infrastructure.repositories.OrdemServicoRepository;
import br.com.fiap.oficina.infrastructure.repositories.OrdemServicoServicoRepository;
import br.com.fiap.oficina.infrastructure.repositories.PecaRepository;
import br.com.fiap.oficina.infrastructure.repositories.ServicoRepository;
import br.com.fiap.oficina.infrastructure.repositories.VeiculoRepository;
import br.com.fiap.oficina.interfaces.dtos.AdicionarPecaOSDTO;
import br.com.fiap.oficina.interfaces.dtos.AdicionarServicoOSDTO;
import br.com.fiap.oficina.interfaces.dtos.AtualizarStatusOSDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoPecaResponseDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoResponseDTO;
import br.com.fiap.oficina.interfaces.dtos.OrdemServicoServicoResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.EstoqueInsuficienteException;
import br.com.fiap.oficina.interfaces.exceptions.OrdemServicoNaoEncontradaException;
import br.com.fiap.oficina.interfaces.exceptions.PecaNaoEncontradaException;
import br.com.fiap.oficina.interfaces.exceptions.ServicoNaoEncontradoException;
import br.com.fiap.oficina.interfaces.exceptions.StatusOrdemServicoInvalidoException;
import br.com.fiap.oficina.interfaces.exceptions.VeiculoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final PecaRepository pecaRepository;
    private final OrdemServicoServicoRepository ordemServicoServicoRepository;
    private final OrdemServicoPecaRepository ordemServicoPecaRepository;

    public OrdemServicoService(
            OrdemServicoRepository ordemServicoRepository,
            VeiculoRepository veiculoRepository,
            ServicoRepository servicoRepository,
            PecaRepository pecaRepository,
            OrdemServicoServicoRepository ordemServicoServicoRepository,
            OrdemServicoPecaRepository ordemServicoPecaRepository) {

        this.ordemServicoRepository = ordemServicoRepository;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
        this.pecaRepository = pecaRepository;
        this.ordemServicoServicoRepository = ordemServicoServicoRepository;
        this.ordemServicoPecaRepository = ordemServicoPecaRepository;
    }

    public OrdemServicoResponseDTO criar(
            OrdemServicoRequestDTO request) {

        Veiculo veiculo = veiculoRepository
                .findById(request.veiculoId())
                .orElseThrow(() ->
                        new VeiculoNaoEncontradoException(
                                "Veículo não encontrado"
                        )
                );

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setVeiculo(veiculo);
        ordemServico.setDescricaoProblema(request.descricaoProblema());
        ordemServico.setStatus(StatusOrdemServico.CRIADA);
        ordemServico.setValorTotal(BigDecimal.ZERO);
        ordemServico.setDataAbertura(LocalDateTime.now());

        OrdemServico salva =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(salva);
    }

    public List<OrdemServicoResponseDTO> listarTodas() {

        return ordemServicoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public OrdemServicoResponseDTO buscarPorId(Long id) {

        OrdemServico ordemServico =
                buscarEntidadePorId(id);

        return converterParaResponse(ordemServico);
    }

    public List<OrdemServicoResponseDTO> buscarPorVeiculo(
            Long veiculoId) {

        if (!veiculoRepository.existsById(veiculoId)) {
            throw new VeiculoNaoEncontradoException(
                    "Veículo não encontrado"
            );
        }

        return ordemServicoRepository
                .findByVeiculoId(veiculoId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public OrdemServicoResponseDTO adicionarServico(
            Long ordemServicoId,
            AdicionarServicoOSDTO request) {

        OrdemServico ordemServico =
                buscarEntidadePorId(ordemServicoId);

        Servico servico = servicoRepository
                .findById(request.servicoId())
                .orElseThrow(() ->
                        new ServicoNaoEncontradoException(
                                "Serviço não encontrado"
                        )
                );

        OrdemServicoServico item =
                new OrdemServicoServico();

        item.setOrdemServico(ordemServico);
        item.setServico(servico);
        item.setPreco(servico.getPreco());

        ordemServicoServicoRepository.save(item);

        BigDecimal novoTotal =
                ordemServico.getValorTotal()
                        .add(servico.getPreco());

        ordemServico.setValorTotal(novoTotal);

        OrdemServico atualizada =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(atualizada);
    }

    @Transactional
    public OrdemServicoResponseDTO adicionarPeca(
            Long ordemServicoId,
            AdicionarPecaOSDTO request) {

        OrdemServico ordemServico =
                buscarEntidadePorId(ordemServicoId);

        Peca peca = pecaRepository
                .findById(request.pecaId())
                .orElseThrow(() ->
                        new PecaNaoEncontradaException(
                                "Peça não encontrada"
                        )
                );

        if (peca.getQuantidadeEstoque() < request.quantidade()) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para adicionar a peça à ordem de serviço"
            );
        }

        BigDecimal quantidade =
                BigDecimal.valueOf(request.quantidade());

        BigDecimal subtotal =
                peca.getPreco().multiply(quantidade);

        OrdemServicoPeca item =
                new OrdemServicoPeca();

        item.setOrdemServico(ordemServico);
        item.setPeca(peca);
        item.setQuantidade(request.quantidade());
        item.setPrecoUnitario(peca.getPreco());
        item.setSubtotal(subtotal);

        ordemServicoPecaRepository.save(item);

        int novoEstoque =
                peca.getQuantidadeEstoque()
                        - request.quantidade();

        peca.setQuantidadeEstoque(novoEstoque);

        pecaRepository.save(peca);

        BigDecimal novoTotal =
                ordemServico.getValorTotal()
                        .add(subtotal);

        ordemServico.setValorTotal(novoTotal);

        OrdemServico atualizada =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(atualizada);
    }

    public OrdemServicoResponseDTO atualizarStatus(
            Long id,
            AtualizarStatusOSDTO request) {

        OrdemServico ordemServico =
                buscarEntidadePorId(id);

        StatusOrdemServico atual =
                ordemServico.getStatus();

        StatusOrdemServico novo =
                request.status();

        validarTransicao(atual, novo);

        ordemServico.setStatus(novo);

        OrdemServico atualizada =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(atualizada);
    }

    private void validarTransicao(
            StatusOrdemServico atual,
            StatusOrdemServico novo) {

        boolean valida = switch (atual) {

            case CRIADA ->
                    novo == StatusOrdemServico.DIAGNOSTICO
                            || novo == StatusOrdemServico.CANCELADA;

            case DIAGNOSTICO ->
                    novo == StatusOrdemServico.AGUARDANDO_APROVACAO
                            || novo == StatusOrdemServico.CANCELADA;

            case AGUARDANDO_APROVACAO ->
                    novo == StatusOrdemServico.APROVADA
                            || novo == StatusOrdemServico.CANCELADA;

            case APROVADA ->
                    novo == StatusOrdemServico.EM_EXECUCAO
                            || novo == StatusOrdemServico.CANCELADA;

            case EM_EXECUCAO ->
                    novo == StatusOrdemServico.FINALIZADA;

            case FINALIZADA ->
                    novo == StatusOrdemServico.ENTREGUE;

            case ENTREGUE, CANCELADA -> false;
        };

        if (!valida) {
            throw new StatusOrdemServicoInvalidoException(
                    "Transição de status inválida: "
                            + atual
                            + " -> "
                            + novo
            );
        }
    }

    public void excluir(Long id) {

        OrdemServico ordemServico =
                buscarEntidadePorId(id);

        ordemServicoRepository.delete(ordemServico);
    }

    private OrdemServico buscarEntidadePorId(Long id) {

        return ordemServicoRepository
                .findById(id)
                .orElseThrow(() ->
                        new OrdemServicoNaoEncontradaException(
                                "Ordem de serviço não encontrada"
                        )
                );
    }

    private OrdemServicoResponseDTO converterParaResponse(
            OrdemServico ordemServico) {

        List<OrdemServicoServicoResponseDTO> servicos =
                ordemServicoServicoRepository
                        .findByOrdemServicoId(ordemServico.getId())
                        .stream()
                        .map(item ->
                                new OrdemServicoServicoResponseDTO(
                                        item.getServico().getId(),
                                        item.getServico().getNome(),
                                        item.getPreco()
                                )
                        )
                        .toList();

        List<OrdemServicoPecaResponseDTO> pecas =
                ordemServicoPecaRepository
                        .findByOrdemServicoId(ordemServico.getId())
                        .stream()
                        .map(item ->
                                new OrdemServicoPecaResponseDTO(
                                        item.getPeca().getId(),
                                        item.getPeca().getNome(),
                                        item.getQuantidade(),
                                        item.getPrecoUnitario(),
                                        item.getSubtotal()
                                )
                        )
                        .toList();

        return new OrdemServicoResponseDTO(
                ordemServico.getId(),
                ordemServico.getVeiculo().getId(),
                ordemServico.getVeiculo().getPlaca(),
                ordemServico.getVeiculo().getCliente().getId(),
                ordemServico.getVeiculo().getCliente().getNome(),
                ordemServico.getDescricaoProblema(),
                ordemServico.getStatus(),
                servicos,
                pecas,
                ordemServico.getValorTotal(),
                ordemServico.getDataAbertura()
        );
    }
}