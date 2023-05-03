package com.example;

import com.squareup.square.exceptions.ApiException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("/hello")
public class GreetingResource {

    @Inject
    SquareBean squareBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws IOException, ApiException {
        squareBean.test();
        return "Hello from RESTEasy Reactive";
    }
}
