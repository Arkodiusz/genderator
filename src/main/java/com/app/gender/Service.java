package com.app.gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.reactive.function.client.WebClient;

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
        LOGGER.info("..Received item: ", designation);
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

    public void save(TokenDto tokenDto) {
        System.out.println();
    }
    public void delete(Long id) {

    }

    public void update(TokenDto tokenDto) {

    }

}
