package br.edu.infnet.exercicio3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da API ViaCEP")
public class ViaCepTest {
    private ViaCepClient client;

    @BeforeEach
    void setUp() {
        client = new ViaCepClient();
    }

    @Test
    @DisplayName("Consulta CEP válido retorna dados corretos")
    void testConsultaCepValido() throws Exception {
        CepResponse response = client.consultarCep("01310100");
        
        assertNotNull(response);
        assertFalse(response.isErro());
        assertEquals("01310-100", response.getCep());
        assertEquals("SP", response.getUf());
        assertEquals("São Paulo", response.getLocalidade());
        assertNotNull(response.getLogradouro());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000000", "99999999", "12345678"})
    @DisplayName("CEP inexistente retorna erro")
    void testCepInexistente(String cep) throws Exception {
        CepResponse response = client.consultarCep(cep);
        assertTrue(response.isErro(), "CEP " + cep + " deve retornar erro");
    }

    @ParameterizedTest
    @CsvSource({
        "abc12345, CEP com letras",
        "1234567, CEP incompleto",
        "123456789, CEP com mais dígitos",
        "'', CEP vazio"
    })
    @DisplayName("Formato de CEP inválido")
    void testFormatoInvalido(String cep, String descricao) {
        assertThrows(Exception.class, () -> {
            client.consultarCep(cep);
        }, descricao);
    }

    @Test
    @DisplayName("CEP com hífen funciona corretamente")
    void testCepComHifen() throws Exception {
        CepResponse response = client.consultarCep("01310-100");
        assertNotNull(response);
        assertFalse(response.isErro());
    }

    @ParameterizedTest
    @CsvSource({
        "01310100, Avenida Paulista, São Paulo, SP",
        "20040020, Praça Quinze de Novembro, Rio de Janeiro, RJ",
        "30130100, Avenida Afonso Pena, Belo Horizonte, MG"
    })
    @DisplayName("Valores limite - CEPs válidos de diferentes estados")
    void testCepsValidosDiferentesEstados(String cep, String logradouroEsperado, 
                                          String cidade, String uf) throws Exception {
        CepResponse response = client.consultarCep(cep);
        
        assertNotNull(response);
        assertFalse(response.isErro());
        assertEquals(uf, response.getUf());
        assertEquals(cidade, response.getLocalidade());
        assertTrue(response.getLogradouro().contains(logradouroEsperado.split(" ")[0]));
    }

    @Test
    @DisplayName("Consulta por endereço válido - Avenida Paulista")
    void testConsultaPorEnderecoValido() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("SP", "Sao Paulo", "Avenida Paulista");
        
        assertNotNull(responses);
        assertTrue(responses.length > 0);
        
        for (CepResponse response : responses) {
            assertEquals("SP", response.getUf());
            assertEquals("São Paulo", response.getLocalidade());
            assertTrue(response.getLogradouro().toLowerCase().contains("paulista"));
        }
    }

    @Test
    @DisplayName("Consulta por endereço com acentuação")
    void testConsultaComAcentuacao() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("SP", "São Paulo", "Avenida Paulista");
        
        assertNotNull(responses);
        assertTrue(responses.length > 0);
    }

    @Test
    @DisplayName("UF inválida retorna array vazio ou erro")
    void testUfInvalida() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("XX", "Cidade", "Logradouro");
        
        assertTrue(responses == null || responses.length == 0);
    }

    @Test
    @DisplayName("Cidade inexistente retorna array vazio")
    void testCidadeInexistente() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("SP", "CidadeInexistente123", "Rua");
        
        assertTrue(responses == null || responses.length == 0);
    }

    @Test
    @DisplayName("Logradouro inexistente retorna array vazio")
    void testLogradouroInexistente() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("SP", "Sao Paulo", "RuaInexistente999");
        
        assertTrue(responses == null || responses.length == 0);
    }

    @ParameterizedTest
    @CsvSource({
        "SP, Sao Paulo, Rua Augusta, true",
        "RJ, Rio de Janeiro, Copacabana, true",
        "XX, Cidade, Rua, false",
        "SP, CidadeInexistente, Rua, false"
    })
    @DisplayName("Tabela de decisão - Combinações de parâmetros válidos/inválidos")
    void testTabelaDecisao(String uf, String cidade, String logradouro, boolean deveRetornarResultados) throws Exception {
        CepResponse[] responses = client.consultarPorEndereco(uf, cidade, logradouro);
        
        if (deveRetornarResultados) {
            assertNotNull(responses);
            assertTrue(responses.length > 0, 
                String.format("Deve retornar resultados para UF=%s, Cidade=%s, Logradouro=%s", 
                    uf, cidade, logradouro));
        } else {
            assertTrue(responses == null || responses.length == 0,
                String.format("Não deve retornar resultados para UF=%s, Cidade=%s, Logradouro=%s", 
                    uf, cidade, logradouro));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "01000000",
        "01999999",
        "99000000",
        "99999998"
    })
    @DisplayName("Partição de equivalência - Faixas de CEP válidas")
    void testParticaoEquivalenciaCep(String cep) throws Exception {
        CepResponse response = client.consultarCep(cep);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Análise de valor limite - CEP mínimo válido")
    void testCepMinimoValido() throws Exception {
        CepResponse response = client.consultarCep("01000000");
        assertNotNull(response);
    }

    @Test
    @DisplayName("Análise de valor limite - CEP máximo teórico")
    void testCepMaximoTeorico() throws Exception {
        CepResponse response = client.consultarCep("99999999");
        assertNotNull(response);
    }

    @Test
    @DisplayName("Consulta com caracteres especiais no logradouro")
    void testCaracteresEspeciais() throws Exception {
        CepResponse[] responses = client.consultarPorEndereco("SP", "Sao Paulo", "Avenida");
        assertNotNull(responses);
    }

    @Test
    @DisplayName("Performance - Múltiplas consultas sequenciais")
    void testMultiplasConsultas() throws Exception {
        String[] ceps = {"01310100", "20040020", "30130100"};
        
        for (String cep : ceps) {
            CepResponse response = client.consultarCep(cep);
            assertNotNull(response);
            assertFalse(response.isErro());
        }
    }
}
