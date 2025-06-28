package com.despesasdeputados.service;

import com.despesasdeputados.model.Deputado;
import com.despesasdeputados.model.Despesa;
import com.despesasdeputados.repository.DeputadoRepository;
import com.despesasdeputados.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class CsvProcessorService {

    @Autowired
    private DeputadoRepository deputadoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    private static final String CSV_URL = "https://www.camara.leg.br/transparencia/api/rest/csv/deputados/2025";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CSV_DELIMITER = ";"; // O delimitador do CSV da Câmara é ponto e vírgula

    @Transactional
    public void processCsvData() throws Exception {
        URL url = new URL(CSV_URL);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            boolean isHeader = true; // Para pular a primeira linha (cabeçalho)

            // Mapeamento dos índices das colunas no CSV da Câmara para os nossos campos
            // É MUITO IMPORTANTE que esses índices correspondam à ordem real das colunas no CSV de 2025.
            // Se a ordem mudar, este código precisará ser ajustado.
            final int INDEX_NOME_DEPUTADO = 6;
            final int INDEX_CPF_DEPUTADO = 7;
            final int INDEX_PARTIDO = 8;
            final int INDEX_UF = 9;
            final int INDEX_VALOR_LIQUIDO = 12; // vlrLiquido
            final int INDEX_URL_DOCUMENTO = 16; // urlDocumento
            final int INDEX_DATA_EMISSAO = 17; // dataEmissao
            final int INDEX_FORNECEDOR = 3;   // txtFornecedor


            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Abordagem simples de split. Talvez falhe se tiver delimitadores ou aspas duplas.
                String[] values = line.split(CSV_DELIMITER, -1); // O '-1' garante que campos vazios no final da linha sejam incluídos

                // Validação básica do número de colunas esperado
                if (values.length <= Math.max(Math.max(INDEX_DATA_EMISSAO, INDEX_FORNECEDOR), Math.max(INDEX_VALOR_LIQUIDO, INDEX_URL_DOCUMENTO))) {
                    System.err.println("Linha CSV com número inesperado de colunas, ignorando: " + line);
                    continue;
                }

                String sgUF = values[INDEX_UF].trim();
                // 1. Filtragem: Ignore linhas que não contenham valor no campo sgUF
                if (sgUF.isEmpty()) {
                    continue;
                }

                String cpfDeputado = values[INDEX_CPF_DEPUTADO].trim();
                if (cpfDeputado.isEmpty()) {
                    System.err.println("Linha CSV sem CPF do deputado, ignorando: " + line);
                    continue; // Pular registros sem CPF para identificação única do deputado
                }

                Optional<Deputado> existingDeputado = deputadoRepository.findByCpf(cpfDeputado);
                Deputado deputado;

                if (existingDeputado.isPresent()) {
                    deputado = existingDeputado.get();
                } else {
                    deputado = new Deputado();
                    deputado.setNome(values[INDEX_NOME_DEPUTADO].trim());
                    deputado.setUf(sgUF);
                    deputado.setCpf(cpfDeputado);
                    deputado.setPartido(values[INDEX_PARTIDO].trim());
                    deputadoRepository.save(deputado);
                }

                Despesa despesa = new Despesa();
                despesa.setDeputado(deputado); // Associar a despesa ao deputado

                try {
                    String dataEmissaoStr = values[INDEX_DATA_EMISSAO].trim();
                    despesa.setDataEmissao(LocalDate.parse(dataEmissaoStr, DATE_FORMATTER));
                } catch (DateTimeParseException e) {
                    System.err.println("Erro ao parsear data '" + values[INDEX_DATA_EMISSAO] + "' na linha: " + line + ". Ignorando despesa. Erro: " + e.getMessage());
                    continue;
                }

                despesa.setFornecedor(values[INDEX_FORNECEDOR].trim());

                try {
                    // 2. Convetendo String para BigDecimal.
                    String valorLiquidoStr = values[INDEX_VALOR_LIQUIDO].trim().replace(",", ".");
                    despesa.setValorLiquido(new BigDecimal(valorLiquidoStr));
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao parsear valor líquido '" + values[INDEX_VALOR_LIQUIDO] + "' na linha: " + line + ". Ignorando despesa. Erro: " + e.getMessage());
                    continue;
                }

                despesa.setUrlDocumento(values[INDEX_URL_DOCUMENTO].trim());

                despesaRepository.save(despesa);
            }
        }
    }
}