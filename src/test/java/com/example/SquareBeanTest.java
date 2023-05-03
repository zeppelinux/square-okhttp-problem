package com.example;

import com.squareup.square.exceptions.ApiException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@QuarkusTest
class SquareBeanTest {

    @Inject
    SquareBean squareBean;

    @Test
    void test() throws IOException, ApiException {
        squareBean.test();
    }
}