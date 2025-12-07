package com.automacao.utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CookieManager {
    private WebDriver driver;
    private String cookieFilePath;

    public CookieManager(WebDriver driver) {
        this.driver = driver;
        this.cookieFilePath = System.getProperty("user.dir") + File.separator + "cookies.txt";
    }

    public CookieManager(WebDriver driver, String filePath) {
        this.driver = driver;
        this.cookieFilePath = filePath;
    }

    public void saveCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cookieFilePath))) {
            for (Cookie cookie : cookies) {
                writer.write(formatCookie(cookie));
                writer.newLine();
            }
            System.out.println("Cookies salvos: " + cookieFilePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar cookies: " + e.getMessage());
        }
    }

    public void loadCookies() {
        File file = new File(cookieFilePath);
        if (!file.exists()) {
            System.out.println("Arquivo de cookies não encontrado: " + cookieFilePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    addCookieFromString(line);
                }
            }
            System.out.println("Cookies carregados de: " + cookieFilePath);
        } catch (IOException e) {
            System.err.println("Erro ao carregar cookies: " + e.getMessage());
        }
    }

    private String formatCookie(Cookie cookie) {
        return String.format("%s=%s|%s|%s|%s|%d|%s",
            cookie.getName(),
            cookie.getValue(),
            cookie.getDomain(),
            cookie.getPath(),
            cookie.isSecure(),
            cookie.getExpiry() != null ? cookie.getExpiry().getTime() : 0,
            cookie.isHttpOnly()
        );
    }

    private void addCookieFromString(String cookieString) {
        String[] parts = cookieString.split("\\|");
        if (parts.length >= 3) {
            String nameValue = parts[0];
            String[] nv = nameValue.split("=", 2);
            String name = nv[0];
            String value = nv.length > 1 ? nv[1] : "";
            String domain = parts[1];
            String path = parts[2];
            boolean secure = parts.length > 3 && Boolean.parseBoolean(parts[3]);
            long expiry = parts.length > 4 ? Long.parseLong(parts[4]) : 0;
            boolean httpOnly = parts.length > 5 && Boolean.parseBoolean(parts[5]);

            Cookie cookie = new Cookie.Builder(name, value)
                .domain(domain)
                .path(path)
                .isSecure(secure)
                .isHttpOnly(httpOnly)
                .expiresOn(expiry > 0 ? new Date(expiry) : null)
                .build();

            try {
                driver.manage().addCookie(cookie);
            } catch (Exception e) {
                System.err.println("Erro ao adicionar cookie: " + e.getMessage());
            }
        }
    }

    public void deleteCookie(String cookieName) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);
        if (cookie != null) {
            driver.manage().deleteCookie(cookie);
        }
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public Cookie getCookie(String cookieName) {
        return driver.manage().getCookieNamed(cookieName);
    }

    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }

    public void printAllCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("\n=== COOKIES ATUAIS ===");
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName() + " = " + cookie.getValue());
        }
        System.out.println("Total de cookies: " + cookies.size() + "\n");
    }

    public boolean cookieExists(String cookieName) {
        return driver.manage().getCookieNamed(cookieName) != null;
    }
}
