package br.com.fiap.oficina.interfaces.dtos;

public record TempoMedioExecucaoDTO(
        long totalOrdensFinalizadas,
        double tempoMedioEmMinutos,
        String tempoMedioFormatado
) {
}
