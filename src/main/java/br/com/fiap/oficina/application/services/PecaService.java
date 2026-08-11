package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.peca.Peca;
import br.com.fiap.oficina.infrastructure.repositories.PecaRepository;
import br.com.fiap.oficina.interfaces.dtos.MovimentacaoEstoqueDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.PecaResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.EstoqueInsuficienteException;
import br.com.fiap.oficina.interfaces.exceptions.PecaDuplicadaException;
import br.com.fiap.oficina.interfaces.exceptions.PecaNaoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;

    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public PecaResponseDTO criar(PecaRequestDTO request) {

        if (pecaRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new PecaDuplicadaException(
                    "Peça já cadastrada com este código"
            );
        }

        Peca peca = new Peca();

        peca.setCodigo(request.codigo());
        peca.setNome(request.nome());
        peca.setDescricao(request.descricao());
        peca.setPreco(request.preco());
        peca.setQuantidadeEstoque(request.quantidadeEstoque());

        Peca salva = pecaRepository.save(peca);

        return converterParaResponse(salva);
    }

    public List<PecaResponseDTO> listarTodos() {

        return pecaRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public PecaResponseDTO buscarPorId(Long id) {

        Peca peca = buscarEntidadePorId(id);

        return converterParaResponse(peca);
    }

    public PecaResponseDTO atualizar(
            Long id,
            PecaRequestDTO request) {

        Peca peca = buscarEntidadePorId(id);

        if (!peca.getCodigo().equalsIgnoreCase(request.codigo())
                && pecaRepository.existsByCodigoIgnoreCase(request.codigo())) {

            throw new PecaDuplicadaException(
                    "Já existe outra peça cadastrada com este código"
            );
        }

        peca.setCodigo(request.codigo());
        peca.setNome(request.nome());
        peca.setDescricao(request.descricao());
        peca.setPreco(request.preco());
        peca.setQuantidadeEstoque(request.quantidadeEstoque());

        Peca atualizada = pecaRepository.save(peca);

        return converterParaResponse(atualizada);
    }

    public void excluir(Long id) {

        Peca peca = buscarEntidadePorId(id);

        pecaRepository.delete(peca);
    }

    public PecaResponseDTO adicionarEstoque(
            Long id,
            MovimentacaoEstoqueDTO request) {

        Peca peca = buscarEntidadePorId(id);

        int novaQuantidade =
                peca.getQuantidadeEstoque() + request.quantidade();

        peca.setQuantidadeEstoque(novaQuantidade);

        Peca atualizada = pecaRepository.save(peca);

        return converterParaResponse(atualizada);
    }

    public PecaResponseDTO baixarEstoque(
            Long id,
            MovimentacaoEstoqueDTO request) {

        Peca peca = buscarEntidadePorId(id);

        if (peca.getQuantidadeEstoque() < request.quantidade()) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para realizar a baixa"
            );
        }

        int novaQuantidade =
                peca.getQuantidadeEstoque() - request.quantidade();

        peca.setQuantidadeEstoque(novaQuantidade);

        Peca atualizada = pecaRepository.save(peca);

        return converterParaResponse(atualizada);
    }

    private Peca buscarEntidadePorId(Long id) {

        return pecaRepository.findById(id)
                .orElseThrow(() ->
                        new PecaNaoEncontradaException(
                                "Peça não encontrada"
                        )
                );
    }

    private PecaResponseDTO converterParaResponse(Peca peca) {

        return new PecaResponseDTO(
                peca.getId(),
                peca.getCodigo(),
                peca.getNome(),
                peca.getDescricao(),
                peca.getPreco(),
                peca.getQuantidadeEstoque()
        );
    }
}