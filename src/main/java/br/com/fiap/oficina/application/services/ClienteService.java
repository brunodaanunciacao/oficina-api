package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.cliente.Cliente;
import br.com.fiap.oficina.infrastructure.repositories.ClienteRepository;
import br.com.fiap.oficina.interfaces.dtos.ClienteRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.ClienteResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.ClienteDuplicadoException;
import br.com.fiap.oficina.interfaces.exceptions.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO criar(ClienteRequestDTO request) {

        if (clienteRepository.existsByCpfCnpj(request.cpfCnpj())) {
            throw new ClienteDuplicadoException(
                    "Cliente já cadastrado com este CPF/CNPJ"
            );
        }

        Cliente cliente = new Cliente();

        cliente.setNome(request.nome());
        cliente.setCpfCnpj(request.cpfCnpj());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());

        Cliente salvo = clienteRepository.save(cliente);

        return new ClienteResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getCpfCnpj(),
                salvo.getEmail(),
                salvo.getTelefone()
        );
    }

    public List<ClienteResponseDTO> listarTodos() {

        return clienteRepository.findAll()
                .stream()
                .map(cliente -> new ClienteResponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpfCnpj(),
                        cliente.getEmail(),
                        cliente.getTelefone()
                ))
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException(
                                "Cliente não encontrado"
                        )
                );

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getEmail(),
                cliente.getTelefone()
        );
    }

    public ClienteResponseDTO atualizar(
            Long id,
            ClienteRequestDTO request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException(
                                "Cliente não encontrado"
                        )
                );

        if (!cliente.getCpfCnpj().equals(request.cpfCnpj())
                && clienteRepository.existsByCpfCnpj(request.cpfCnpj())) {

            throw new ClienteDuplicadoException(
                    "Já existe outro cliente cadastrado com este CPF/CNPJ"
            );
        }

        cliente.setNome(request.nome());
        cliente.setCpfCnpj(request.cpfCnpj());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());

        Cliente atualizado = clienteRepository.save(cliente);

        return new ClienteResponseDTO(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getCpfCnpj(),
                atualizado.getEmail(),
                atualizado.getTelefone()
        );
    }

    public void excluir(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException(
                                "Cliente não encontrado"
                        )
                );

        clienteRepository.delete(cliente);
    }
}