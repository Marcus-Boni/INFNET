public class Livro {
    
    private String titulo;
    private boolean disponivel;

    public Livro(String titulo) {
        this.titulo = titulo;
        this.disponivel = true; 
    }

    public void emprestar() {
        if (this.disponivel) {
            this.disponivel = false;
        } else {
            throw new IllegalStateException("Livro já está emprestado.");
        }
    }

    public void devolver() {
        this.disponivel = true;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public boolean estaDisponivel() {
        return this.disponivel;
    }
}