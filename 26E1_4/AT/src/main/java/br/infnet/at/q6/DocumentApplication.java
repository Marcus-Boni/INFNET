package br.infnet.at.q6;

import java.util.List;

public class DocumentApplication {
    public static void main(String[] args) {
        List<Document> documents = List.of(
            new PdfDocument(),
            new HtmlDocument(),
            new MarkdownDocument()
        );

        for (Document document : documents) {
            document.print();
        }
    }
}
