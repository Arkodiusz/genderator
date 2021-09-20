package com.app.gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private static final String URL = "http://localhost:8080/api/gender";

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    public String detect(String name) {
        LOGGER.info("detecting gender..");
        WebClient.RequestHeadersSpec<?> spec = WebClient.create().
                get().uri(URL + "/" + name);
        String designation = spec.retrieve().
                toEntity(Gender.class).block().getBody().getDesignation();
        LOGGER.info("..Received item: ", designation);
        return designation;
    }

    public List<String> listTokens(String gender) {
        LOGGER.info("fetching " + gender + " tokens..");
        WebClient.RequestHeadersSpec<?> spec = WebClient.create().
                get().uri(URL + "/tokens/" + gender);
        List<String> tokens = spec.retrieve().
                toEntityList(String.class).block().getBody();
        LOGGER.info("..received {} items.", tokens.size());
        return tokens;

    }
}
