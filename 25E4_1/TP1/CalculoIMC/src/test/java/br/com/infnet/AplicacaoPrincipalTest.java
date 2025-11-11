package br.com.infnet;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test; 

class AplicacaoPrincipalTest {

    @Test
    void testCalculoComMock() {
        IMCApiServico servicoMock = mock(IMCApiServico.class);

        when(servicoMock.calcularIMC(80, 1.80)).thenReturn(24.69);

        AplicacaoPrincipal app = new AplicacaoPrincipal(servicoMock);
        
        String resultado = app.obterClassificacaoFormatada(80, 1.80);

        assertThat(resultado).isEqualTo("OK");
    }
}