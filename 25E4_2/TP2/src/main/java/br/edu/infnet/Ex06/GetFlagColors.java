import java.util.Arrays;
import java.util.List;

// Classe fictícia para as cores
class Color {
    public static final String RED = "Red";
    public static final String WHITE = "White";
    public static final String BLUE = "Blue";
    public static final String BLACK = "Black";
    public static final String YELLOW = "Yellow";
    public static final String GREEN = "Green";
    public static final String GRAY = "Gray";
}

// 1. Refatoração da Enumeração para conter o comportamento
public enum Nationality {
    // Explicação: Cada enum agora se autodescreve e contém a lógica para as suas cores.
    DUTCH(Arrays.asList(Color.RED, Color.WHITE, Color.BLUE)),
    GERMAN(Arrays.asList(Color.BLACK, Color.RED, Color.YELLOW)),
    BELGIAN(Arrays.asList(Color.BLACK, Color.YELLOW, Color.RED)),
    FRENCH(Arrays.asList(Color.BLUE, Color.WHITE, Color.RED)),
    ITALIAN(Arrays.asList(Color.GREEN, Color.WHITE, Color.RED)),
    
    // O caso UNCLASSIFIED também é uma constante com seu próprio comportamento padrão.
    UNCLASSIFIED(Arrays.asList(Color.GRAY)); 

    private final List<String> flagColors;

    Nationality(List<String> flagColors) {
        this.flagColors = flagColors;
    }

    // Explicação: Getter simples que expõe o comportamento encapsulado.
    public List<String> getFlagColors() {
        return flagColors;
    }
}

// 2. Refatoração da Classe de Serviço
public class NotificacaoServiceRefatorado {
    public List<String> getFlagColors(Nationality nationality) {
        // Explicação: Não há lógica condicional (switch/case) aqui. O polimorfismo implícito
        // da enumeração trata da seleção das cores.
        return nationality.getFlagColors();
    }
}

/*
Explicação dos Impactos do Código Fortemente Acoplado e Melhoria:
* **Problema de Acoplamento:** O `switch/case` centraliza a lógica de seleção de cores. Isso significa que a classe de serviço tem que "saber" a lista de cores para *todas* as nacionalidades, gerando alto acoplamento entre o serviço e os detalhes de implementação de cada nacionalidade.
* **Melhoria (Polimorfismo/Enumeração):** O comportamento (`getFlagColors`) foi movido e encapsulado dentro de cada membro da enumeração.
    * **OCP Cumprido:** Se uma nova nacionalidade (ex: `SPANISH`) for adicionada, basta adicionar o novo membro à enumeração com suas cores, e o método `getFlagColors` no serviço **não precisa ser alterado**.
    * **Acoplamento Reduzido:** A classe de serviço apenas chama o método no objeto `Nationality`, delegando a responsabilidade de saber suas próprias cores para o objeto em si.
*/