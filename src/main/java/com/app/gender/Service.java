package com.app.gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private static final String URL = "http://localhost:8080/api/";

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private static Service service;

    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    public String detect(String name) {
        LOGGER.info("detecting gender..");
        WebClient.RequestHeadersSpec<?> spec = WebClient.create().
                get().uri(URL + "gender/" + name);
        String designation = spec.retrieve().
                toEntity(String.class).block().getBody();
        LOGGER.info("..received item: ", designation);
        return designation;
    }

    public List<TokenDto> listTokens(String gender) {
        LOGGER.info("fetching " + gender + " tokens..");
        WebClient.RequestHeadersSpec<?> spec = WebClient.create().
                get().uri(URL + "tokens/" + gender);
        List<TokenDto> tokens = spec.retrieve().
                toEntityList(TokenDto.class).block().getBody();
        LOGGER.info("..received {} items.", tokens.size());
        return tokens;
    }

    public void save(TokenDto tokenDto) throws IOException {
        LOGGER.info("saving new token..");
        String jsonInputString = "{\"id\": \"" + tokenDto.getId() + "\", \"name\": \"" + tokenDto.getName() + "\", \"gender\": \"" + tokenDto.getGender() + "\"}";
        LOGGER.info(jsonInputString);
        call(URL + "/tokens", jsonInputString, "POST");
        LOGGER.info("..token saved.");
    }


    public void delete(Long id) {
        LOGGER.info("deleting token..");
        RestTemplate rest = new RestTemplate();
        rest.exchange(
                String.format(URL + "/tokens/" + id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class);
        LOGGER.info("..token deleted.");
    }

    public void update(TokenDto tokenDto) throws IOException {
        LOGGER.info("saving new token..");
        String jsonInputString = "{\"id\": \"" + tokenDto.getId() + "\", \"name\": \"" + tokenDto.getName() + "\", \"gender\": \"" + tokenDto.getGender() + "\"}";
        LOGGER.info(jsonInputString);
        call(URL + "/tokens", jsonInputString, "PUT");
        LOGGER.info("..token saved.");
    }

    protected void call(String address, String jsonInputString, String method) throws IOException {

        java.net.URL url = new URL (address);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(method);

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        LOGGER.info("{}", code);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            LOGGER.info("{}", response);
        }
    }
}
