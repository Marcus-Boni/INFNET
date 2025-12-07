@echo off
REM Script para executar os testes de automação Selenium

echo.
echo ====================================================
echo   AUTOMAÇÃO DE TESTES - SELENIUM WEBDRIVER
echo ====================================================
echo.

if "%1"=="" goto menu
if /i "%1"=="3" goto ex3
if /i "%1"=="4" goto ex4
if /i "%1"=="5" goto ex5
if /i "%1"=="6" goto ex6
if /i "%1"=="7" goto ex7
if /i "%1"=="8" goto ex8
if /i "%1"=="9" goto ex9
if /i "%1"=="all" goto all
if /i "%1"=="clean" goto clean

:menu
echo Selecione qual exercício executar:
echo.
echo 3 - Exercício 3: Interação com Elementos Web
echo 4 - Exercício 4: Navegação e Login
echo 5 - Exercício 5: Formulários e Componentes
echo 6 - Exercício 6: Cookies e Sessões
echo 7 - Exercício 7: Captura de Screenshots
echo 8 - Exercício 8: Carrinho e Checkout
echo 9 - Exercício 9: Scroll e Visibilidade
echo all - Executar todos os testes
echo clean - Limpar e reconstrói o projeto
echo.
echo Uso: run.bat [opção]
echo Exemplo: run.bat 3
echo.
goto fim

:ex3
echo Executando EXERCÍCIO 3 - Interação com Elementos Web...
mvn clean test -Dtest=Exercise3InteractionTest
goto fim

:ex4
echo Executando EXERCÍCIO 4 - Navegação e Login...
mvn clean test -Dtest=Exercise4LoginTest
goto fim

:ex5
echo Executando EXERCÍCIO 5 - Formulários e Componentes...
mvn clean test -Dtest=Exercise5FormTest
goto fim

:ex6
echo Executando EXERCÍCIO 6 - Cookies e Sessões...
mvn clean test -Dtest=Exercise6CookiesTest
goto fim

:ex7
echo Executando EXERCÍCIO 7 - Captura de Screenshots...
mvn clean test -Dtest=Exercise7ScreenshotsTest
goto fim

:ex8
echo Executando EXERCÍCIO 8 - Carrinho e Checkout...
mvn clean test -Dtest=Exercise8CartCheckoutTest
goto fim

:ex9
echo Executando EXERCÍCIO 9 - Scroll e Visibilidade...
mvn clean test -Dtest=Exercise9ScrollVisibilityTest
goto fim

:all
echo Executando TODOS os testes...
mvn clean test
goto fim

:clean
echo Limpando e reconstruindo projeto...
mvn clean install
echo Projeto reconstruído com sucesso!
goto fim

:fim
echo.
echo Execução concluída!
echo Verifique os relatórios em: target/surefire-reports/index.html
echo.
