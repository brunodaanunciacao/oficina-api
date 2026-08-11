package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.servico.Servico;
import br.com.fiap.oficina.infrastructure.repositories.ServicoRepository;
import br.com.fiap.oficina.interfaces.dtos.ServicoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.ServicoResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.ServicoDuplicadoException;
import br.com.fiap.oficina.interfaces.exceptions.ServicoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public ServicoResponseDTO criar(ServicoRequestDTO request) {

        if (servicoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new ServicoDuplicadoException(
                    "Serviço já cadastrado com este nome"
            );
        }

        Servico servico = new Servico();

        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());

        Servico salvo = servicoRepository.save(servico);

        return converterParaResponse(salvo);
    }

    public List<ServicoResponseDTO> listarTodos() {

        return servicoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public ServicoResponseDTO buscarPorId(Long id) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() ->
                        new ServicoNaoEncontradoException(
                                "Serviço não encontrado"
                        )
                );

        return converterParaResponse(servico);
    }

    public ServicoResponseDTO atualizar(
            Long id,
            ServicoRequestDTO request) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() ->
                        new ServicoNaoEncontradoException(
                                "Serviço não encontrado"
                        )
                );

        if (!servico.getNome().equalsIgnoreCase(request.nome())
                && servicoRepository.existsByNomeIgnoreCase(request.nome())) {

            throw new ServicoDuplicadoException(
                    "Já existe outro serviço cadastrado com este nome"
            );
        }

        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());

        Servico atualizado = servicoRepository.save(servico);

        return converterParaResponse(atualizado);
    }

    public void excluir(Long id) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() ->
                        new ServicoNaoEncontradoException(
                                "Serviço não encontrado"
                        )
                );

        servicoRepository.delete(servico);
    }

    private ServicoResponseDTO converterParaResponse(Servico servico) {

        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco()
        );
    }
}