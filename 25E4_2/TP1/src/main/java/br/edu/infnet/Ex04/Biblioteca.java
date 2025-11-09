import java.util.ArrayList;
import java.util.List;

public class Biblioteca {

    private List<Livro> acervo;

    public Biblioteca() {
        this.acervo = new ArrayList<>();
    }

    public void adicionarLivro(String titulo) {
        Livro novoLivro = new Livro(titulo);
        this.acervo.add(novoLivro);
    }

    public boolean emprestarLivro(String titulo) {
        Livro livro = buscarLivro(titulo);

        if (livro != null && livro.estaDisponivel()) {
            livro.emprestar(); 
            return true;
        }
        return false; 
    }

    public boolean devolverLivro(String titulo) {
        Livro livro = buscarLivro(titulo);

        if (livro != null) {
            livro.devolver(); 
            return true;
        }
        return false;
    }

    private Livro buscarLivro(String titulo) {
        for (Livro livro : this.acervo) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                return livro;
            }
        }
        return null; 
    }
}