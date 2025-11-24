package com.tp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes CRUD de CustomerService com Partição de Equivalência e Análise de Valor Limite")
class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }

    @Nested
    @DisplayName("Testes de Cadastro - Análise de Valor Limite (Idade)")
    class RegisterCustomerBoundaryTests {

        @Test
        @DisplayName("Deve REJEITAR cadastro com idade 17 (abaixo do limite mínimo)")
        void testRegisterCustomer_Age17_BelowMinimum_ReturnsFalse() {
            Customer customer = new Customer(1, "João Silva", "joao@example.com", 17, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cliente com idade 17 não deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve ACEITAR cadastro com idade 18 (limite mínimo válido)")
        void testRegisterCustomer_Age18_MinimumBoundary_ReturnsTrue() {
            Customer customer = new Customer(2, "Maria Santos", "maria@example.com", 18, true);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "Cliente com idade 18 deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve ACEITAR cadastro com idade 99 (limite máximo válido)")
        void testRegisterCustomer_Age99_MaximumBoundary_ReturnsTrue() {
            Customer customer = new Customer(3, "Pedro Oliveira", "pedro@example.com", 99, true);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "Cliente com idade 99 deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve REJEITAR cadastro com idade 100 (acima do limite máximo)")
        void testRegisterCustomer_Age100_AboveMaximum_ReturnsFalse() {
            Customer customer = new Customer(4, "Ana Costa", "ana@example.com", 100, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cliente com idade 100 não deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve ACEITAR cadastro com idade 50 (valor médio válido)")
        void testRegisterCustomer_Age50_ValidMiddleValue_ReturnsTrue() {
            Customer customer = new Customer(5, "Carlos Souza", "carlos@example.com", 50, true);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "Cliente com idade 50 deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve REJEITAR cadastro com idade 0 (valor extremo inválido)")
        void testRegisterCustomer_Age0_ExtremeInvalid_ReturnsFalse() {
            Customer customer = new Customer(6, "Teste Zero", "zero@example.com", 0, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cliente com idade 0 não deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve REJEITAR cadastro com idade negativa")
        void testRegisterCustomer_NegativeAge_ReturnsFalse() {
            Customer customer = new Customer(7, "Teste Negativo", "negativo@example.com", -5, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cliente com idade negativa não deveria ser cadastrado");
        }
    }

    @Nested
    @DisplayName("Testes de Validação de E-mail - Partição de Equivalência")
    class EmailValidationTests {

        @Test
        @DisplayName("Deve ACEITAR e-mail válido (formato correto)")
        void testRegisterCustomer_ValidEmail_ReturnsTrue() {
            Customer customer = new Customer(10, "Email Válido", "usuario@dominio.com", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "E-mail válido deveria ser aceito");
        }

        @Test
        @DisplayName("Deve ACEITAR e-mail com hífen e underscore")
        void testRegisterCustomer_EmailWithHyphenAndUnderscore_ReturnsTrue() {
            Customer customer = new Customer(11, "Email Especial", "user-name_test@example.com", 25, true);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "E-mail com hífen e underscore deveria ser aceito");
        }

        @Test
        @DisplayName("Deve REJEITAR e-mail sem @ (partição inválida)")
        void testRegisterCustomer_EmailWithoutAt_ReturnsFalse() {
            Customer customer = new Customer(12, "Sem Arroba", "usuariodominio.com", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "E-mail sem @ não deveria ser aceito");
        }

        @Test
        @DisplayName("Deve REJEITAR e-mail sem domínio (partição inválida)")
        void testRegisterCustomer_EmailWithoutDomain_ReturnsFalse() {
            Customer customer = new Customer(13, "Sem Domínio", "usuario@.com", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "E-mail sem domínio não deveria ser aceito");
        }

        @Test
        @DisplayName("Deve REJEITAR e-mail sem extensão (partição inválida)")
        void testRegisterCustomer_EmailWithoutExtension_ReturnsFalse() {
            Customer customer = new Customer(14, "Sem Extensão", "usuario@dominio", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "E-mail sem extensão não deveria ser aceito");
        }

        @Test
        @DisplayName("Deve REJEITAR e-mail com espaços (partição inválida)")
        void testRegisterCustomer_EmailWithSpaces_ReturnsFalse() {
            Customer customer = new Customer(15, "Com Espaços", "usuario nome@dominio.com", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "E-mail com espaços não deveria ser aceito");
        }

        @Test
        @DisplayName("Deve REJEITAR e-mail vazio")
        void testRegisterCustomer_EmptyEmail_ReturnsFalse() {
            Customer customer = new Customer(16, "Email Vazio", "", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "E-mail vazio não deveria ser aceito");
        }
    }

    @Nested
    @DisplayName("Testes de Atualização - Partição de Equivalência (Status)")
    class UpdateCustomerTests {

        @Test
        @DisplayName("Deve PERMITIR atualização de cliente ATIVO")
        void testUpdateCustomer_ActiveCustomer_ReturnsTrue() {
            Customer customer = new Customer(20, "Cliente Ativo", "ativo@example.com", 30, true);

            boolean result = customerService.updateCustomer(customer, "Novo Nome", "novo@example.com", 35);

            assertTrue(result, "Cliente ativo deveria poder ser atualizado");
            assertEquals("Novo Nome", customer.getName());
            assertEquals("novo@example.com", customer.getEmail());
            assertEquals(35, customer.getAge());
        }

        @Test
        @DisplayName("Deve REJEITAR atualização de cliente INATIVO")
        void testUpdateCustomer_InactiveCustomer_ReturnsFalse() {
            Customer customer = new Customer(21, "Cliente Inativo", "inativo@example.com", 30, false);
            String originalName = customer.getName();
            String originalEmail = customer.getEmail();
            int originalAge = customer.getAge();

            boolean result = customerService.updateCustomer(customer, "Tentativa Nome", "tentativa@example.com", 40);

            assertFalse(result, "Cliente inativo não deveria poder ser atualizado");
            assertEquals(originalName, customer.getName());
            assertEquals(originalEmail, customer.getEmail());
            assertEquals(originalAge, customer.getAge());
        }

        @Test
        @DisplayName("Deve atualizar todos os campos de cliente ativo corretamente")
        void testUpdateCustomer_UpdateAllFields_Success() {
            Customer customer = new Customer(22, "Original", "original@example.com", 25, true);

            boolean result = customerService.updateCustomer(customer, "Atualizado", "atualizado@example.com", 30);

            assertTrue(result);
            assertEquals("Atualizado", customer.getName());
            assertEquals("atualizado@example.com", customer.getEmail());
            assertEquals(30, customer.getAge());
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão - Partição de Equivalência (Status)")
    class DeleteCustomerTests {

        @Test
        @DisplayName("Deve PERMITIR exclusão de cliente ATIVO")
        void testDeleteCustomer_ActiveCustomer_ReturnsTrue() {
            Customer customer = new Customer(30, "Cliente Ativo", "ativo@example.com", 40, true);

            boolean result = customerService.deleteCustomer(customer);

            assertTrue(result, "Cliente ativo deveria poder ser excluído");
        }

        @Test
        @DisplayName("Deve REJEITAR exclusão de cliente INATIVO")
        void testDeleteCustomer_InactiveCustomer_ReturnsFalse() {
            Customer customer = new Customer(31, "Cliente Inativo", "inativo@example.com", 40, false);

            boolean result = customerService.deleteCustomer(customer);

            assertFalse(result, "Cliente inativo não deveria poder ser excluído");
        }
    }

    @Nested
    @DisplayName("Testes de Cadastro Completo - Cenários Integrados")
    class CompleteRegistrationTests {

        @Test
        @DisplayName("Deve cadastrar cliente com TODOS os campos VÁLIDOS")
        void testRegisterCustomer_AllFieldsValid_Success() {
            Customer customer = new Customer(
                100,
                "Cliente Completo",
                "completo@example.com",
                45,
                true
            );

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "Cliente com todos os campos válidos deveria ser cadastrado");
        }

        @Test
        @DisplayName("Deve rejeitar cadastro com idade válida mas e-mail inválido")
        void testRegisterCustomer_ValidAge_InvalidEmail_ReturnsFalse() {
            Customer customer = new Customer(101, "Teste", "emailinvalido.com", 30, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cadastro deveria falhar devido ao e-mail inválido");
        }

        @Test
        @DisplayName("Deve rejeitar cadastro com e-mail válido mas idade inválida")
        void testRegisterCustomer_ValidEmail_InvalidAge_ReturnsFalse() {
            Customer customer = new Customer(102, "Teste", "teste@example.com", 150, true);

            boolean result = customerService.registerCustomer(customer);

            assertFalse(result, "Cadastro deveria falhar devido à idade inválida");
        }

        @Test
        @DisplayName("Deve aceitar cadastro de cliente inativo com dados válidos")
        void testRegisterCustomer_InactiveCustomer_ValidData_ReturnsTrue() {
            Customer customer = new Customer(103, "Inativo", "inativo@example.com", 50, false);

            boolean result = customerService.registerCustomer(customer);

            assertTrue(result, "Cliente inativo com dados válidos deveria ser cadastrado");
        }
    }
}
