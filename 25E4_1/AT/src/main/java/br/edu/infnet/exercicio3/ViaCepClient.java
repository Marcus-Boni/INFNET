package br.edu.infnet.exercicio3;

import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class ViaCepClient {
    private static final String BASE_URL = "https://viacep.com.br/ws";
    private final Gson gson;

    public ViaCepClient() {
        this.gson = new Gson();
    }

    public CepResponse consultarCep(String cep) throws Exception {
        String url = String.format("%s/%s/json/", BASE_URL, cep);
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            return client.execute(request, response -> {
                String json = EntityUtils.toString(response.getEntity());
                return gson.fromJson(json, CepResponse.class);
            });
        }
    }

    public CepResponse[] consultarPorEndereco(String uf, String cidade, String logradouro) throws Exception {
        String url = String.format("%s/%s/%s/%s/json/", BASE_URL, uf, cidade, logradouro);
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            return client.execute(request, response -> {
                String json = EntityUtils.toString(response.getEntity());
                return gson.fromJson(json, CepResponse[].class);
            });
        }
    }
}
