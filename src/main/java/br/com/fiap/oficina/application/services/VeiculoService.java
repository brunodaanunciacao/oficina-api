package br.com.fiap.oficina.application.services;

import br.com.fiap.oficina.domain.cliente.Cliente;
import br.com.fiap.oficina.domain.veiculo.Veiculo;
import br.com.fiap.oficina.infrastructure.repositories.ClienteRepository;
import br.com.fiap.oficina.infrastructure.repositories.VeiculoRepository;
import br.com.fiap.oficina.interfaces.dtos.VeiculoRequestDTO;
import br.com.fiap.oficina.interfaces.dtos.VeiculoResponseDTO;
import br.com.fiap.oficina.interfaces.exceptions.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoService(
            VeiculoRepository veiculoRepository,
            ClienteRepository clienteRepository) {

        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public VeiculoResponseDTO criar(VeiculoRequestDTO request) {

        if (veiculoRepository.existsByPlaca(request.placa())) {
            throw new IllegalArgumentException(
                    "Veículo já cadastrado com esta placa"
            );
        }

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException(
                                "Cliente não encontrado"
                        )
                );

        Veiculo veiculo = new Veiculo();

        veiculo.setPlaca(request.placa());
        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setAno(request.ano());
        veiculo.setCliente(cliente);

        Veiculo salvo = veiculoRepository.save(veiculo);

        return converterParaResponse(salvo);
    }

    public List<VeiculoResponseDTO> listarTodos() {

        return veiculoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public VeiculoResponseDTO buscarPorId(Long id) {

        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Veículo não encontrado"
                        )
                );

        return converterParaResponse(veiculo);
    }

    public List<VeiculoResponseDTO> buscarPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNaoEncontradoException(
                    "Cliente não encontrado"
            );
        }

        return veiculoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    private VeiculoResponseDTO converterParaResponse(Veiculo veiculo) {

        return new VeiculoResponseDTO(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCliente().getId(),
                veiculo.getCliente().getNome()
        );
    }
}