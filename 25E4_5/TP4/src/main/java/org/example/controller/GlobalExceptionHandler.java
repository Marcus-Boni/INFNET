package org.example.controller;

import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Tratador global de exceções — fail-gracefully centralizado.
 * Nunca expõe stacktraces ou detalhes internos ao usuário.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ProdutoNotFoundException ex, Model model) {
        model.addAttribute("codigo", 404);
        model.addAttribute("mensagem", ex.getMessage());
        return "erro";
    }

    @ExceptionHandler(NegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNegocio(NegocioException ex, Model model) {
        model.addAttribute("codigo", 400);
        model.addAttribute("mensagem", ex.getMessage());
        return "erro";
    }

    /** Parametro de path com tipo invalido (ex: /produtos/abc em vez de /produtos/1). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("codigo", 400);
        model.addAttribute("mensagem", "Parametro invalido: o valor informado nao e do tipo esperado.");
        return "erro";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        // Fail-gracefully: registra internamente mas não expõe detalhes ao usuário
        model.addAttribute("codigo", 500);
        model.addAttribute("mensagem", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        return "erro";
    }
}
