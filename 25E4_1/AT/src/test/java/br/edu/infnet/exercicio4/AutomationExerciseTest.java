package br.edu.infnet.exercicio4;

import br.edu.infnet.exercicio4.pages.HomePage;
import br.edu.infnet.exercicio4.pages.SignupLoginPage;
import br.edu.infnet.exercicio4.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Automação - AutomationExercise")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationExerciseTest {
    private static WebDriver driver;
    private HomePage homePage;
    private SignupLoginPage signupLoginPage;
    private SignupPage signupPage;
    
    private static final String TEST_EMAIL = "teste" + System.currentTimeMillis() + "@example.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_PASSWORD = "Test@123";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        homePage = new HomePage(driver);
        signupLoginPage = new SignupLoginPage(driver);
        signupPage = new SignupPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Cadastro de novo usuário com sucesso")
    void testCadastroNovoUsuario() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillSignupForm(TEST_NAME, TEST_EMAIL);
            signupLoginPage.clickSignupButton();
            
            signupPage.selectGender("Mr");
            signupPage.fillAccountInformation(TEST_PASSWORD, "15", "5", "1990");
            signupPage.fillAddressInformation(
                "John", "Doe", "Test Company", 
                "123 Test St", "Apt 4B", "United States",
                "California", "Los Angeles", "90001", "1234567890"
            );
            signupPage.clickCreateAccount();
            
            assertTrue(signupPage.isAccountCreated(), "Conta deve ser criada com sucesso");
            signupPage.clickContinue();
            
            assertTrue(homePage.isUserLoggedIn(TEST_NAME), "Usuário deve estar logado");
        } catch (Exception e) {
            signupPage.takeScreenshot("cadastro_falhou_" + System.currentTimeMillis());
            fail("Teste de cadastro falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Login com credenciais válidas")
    void testLoginCredenciaisValidas() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillLoginForm("testuser@example.com", "ValidPassword123");
            signupLoginPage.clickLoginButton();
            
        } catch (Exception e) {
            signupLoginPage.takeScreenshot("login_valido_falhou_" + System.currentTimeMillis());
            fail("Teste de login válido falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Login com email inválido")
    void testLoginEmailInvalido() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillLoginForm("emailinvalido@example.com", "SenhaQualquer123");
            signupLoginPage.clickLoginButton();
            
            assertTrue(signupLoginPage.isLoginErrorDisplayed(), 
                "Mensagem de erro deve ser exibida para email inválido");
            assertEquals("Your email or password is incorrect!", 
                signupLoginPage.getLoginErrorMessage());
        } catch (Exception e) {
            signupLoginPage.takeScreenshot("login_email_invalido_falhou_" + System.currentTimeMillis());
            fail("Teste de login com email inválido falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login com senha incorreta")
    void testLoginSenhaIncorreta() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillLoginForm("testuser@example.com", "SenhaErrada123");
            signupLoginPage.clickLoginButton();
            
            assertTrue(signupLoginPage.isLoginErrorDisplayed(), 
                "Mensagem de erro deve ser exibida para senha incorreta");
        } catch (Exception e) {
            signupLoginPage.takeScreenshot("login_senha_incorreta_falhou_" + System.currentTimeMillis());
            fail("Teste de login com senha incorreta falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Login com campos vazios")
    void testLoginCamposVazios() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillLoginForm("", "");
            signupLoginPage.clickLoginButton();
            
        } catch (Exception e) {
            signupLoginPage.takeScreenshot("login_campos_vazios_falhou_" + System.currentTimeMillis());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Cadastro com email já existente")
    void testCadastroEmailExistente() {
        try {
            homePage.navigateTo();
            homePage.clickSignupLogin();
            
            signupLoginPage.fillSignupForm("Test User", "testuser@example.com");
            signupLoginPage.clickSignupButton();
            
            assertTrue(signupLoginPage.isSignupErrorDisplayed(), 
                "Mensagem de erro deve ser exibida para email já existente");
        } catch (Exception e) {
            signupLoginPage.takeScreenshot("cadastro_email_existente_falhou_" + System.currentTimeMillis());
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
