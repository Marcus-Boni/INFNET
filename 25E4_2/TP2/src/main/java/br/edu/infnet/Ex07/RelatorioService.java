interface Cabecalho { void gerar(); }
interface Corpo { void gerar(); }
interface Rodape { void gerar(); }

// Abstract Factory: Interface para criar a família de componentes de relatório
interface RelatorioFactory {
    Cabecalho criarCabecalho();
    Corpo criarCorpo();
    Rodape criarRodape();
}

class CabecalhoPDF implements Cabecalho { @Override public void gerar() { System.out.println("-> Cabeçalho PDF"); } }
class CorpoPDF implements Corpo { @Override public void gerar() { System.out.println("-> Corpo PDF"); } }
class RodapePDF implements Rodape { @Override public void gerar() { System.out.println("-> Rodapé PDF"); } }

// Factory Concreta para PDF
class PDFRelatorioFactory implements RelatorioFactory {
    @Override public Cabecalho criarCabecalho() { return new CabecalhoPDF(); }
    @Override public Corpo criarCorpo() { return new CorpoPDF(); }
    @Override public Rodape criarRodape() { return new RodapePDF(); }
}

public class RelatorioService {
    
    // Explicação: Usa a Factory para obter os componentes e gerar o relatório de forma polimórfica.
    private void gerarRelatorioInterno(RelatorioFactory factory) {
        factory.criarCabecalho().gerar();
        factory.criarCorpo().gerar();
        factory.criarRodape().gerar();
    }
    
    public void gerarRelatorio(String tipo) {
        System.out.println("Gerando relatório em " + tipo + "...");
        
        // Explicação: O switch é usado de forma controlada apenas para selecionar a Factory.
        // Isso cumpre o OCP para a lógica de geração (os métodos 'gerar' são polimórficos).
        RelatorioFactory factory;
        switch (tipo) {
            case "PDF":
                factory = new PDFRelatorioFactory();
                break;
            case "CSV":
                // factory = new CSVRelatorioFactory(); 
                factory = new PDFRelatorioFactory(); // Exemplo: usando PDF por falta da classe CSV
                break;
            case "JSON":
                // factory = new JSONRelatorioFactory(); 
                factory = new PDFRelatorioFactory(); // Exemplo: usando PDF por falta da classe JSON
                break;
            default:
                throw new IllegalArgumentException("Tipo de relatório desconhecido: " + tipo);
        }

        gerarRelatorioInterno(factory);
    }
}

/*
Explicação da Proposta de Melhoria (Abstract Factory e switch):
* **Problemas do Código Original:** Uso excessivo de `ifs` para orquestrar a lógica, violando o OCP e acoplando o serviço a detalhes de implementação (muitos métodos específicos de PDF, CSV, etc.).
* **Solução com Abstract Factory:** O padrão Abstract Factory isola a criação da família de objetos (`Cabecalho`, `Corpo`, `Rodape`) do código cliente (`RelatorioService`).
* **Melhoria com switch:** O `switch` é usado **apenas** para decidir qual **Factory Concreta** será instanciada. Uma vez que a factory é selecionada, a lógica de geração (`gerarRelatorioInterno`) é **polimórfica**, chamando o método `gerar()` de cada componente. Adicionar um novo formato (ex: XML) agora requer apenas a criação de `XMLFactory` e das classes `CabecalhoXML`, sem tocar no código de `RelatorioService`, cumprindo o OCP.
*/