package com.example;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.CatalogApi;
import com.squareup.square.api.CheckoutApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class SquareBean implements Serializable {

    SquareClient client;

    @Inject
    @ConfigProperty(name = "square.accessToken")
    String accessToken;
    @PostConstruct
    public void init(){
        client = new SquareClient.Builder()
                .accessToken(accessToken)
                .environment(Environment.SANDBOX)
                .build();
    }

    public void test() throws IOException, ApiException {

        List<CatalogItem> catalogItems = new ArrayList<>();
        CatalogApi api = client.getCatalogApi();
        ListCatalogResponse resp = api.listCatalog(null, "ITEM", null);
        for (CatalogObject obj : resp.getObjects()) {
            CatalogItem item = obj.getItemData();
            catalogItems.add(item);
        }

        CheckoutApi checkoutApi = client.getCheckoutApi();

        ListPaymentLinksResponse lpsresp = checkoutApi.listPaymentLinks(null, null);

        CreatePaymentLinkRequest body = new CreatePaymentLinkRequest.Builder()
                .checkoutOptions(new CheckoutOptions.Builder()
                        .acceptedPaymentMethods(new AcceptedPaymentMethods.Builder().applePay(true).googlePay(true).build()).build())
                .prePopulatedData(new PrePopulatedData.Builder().buyerEmail("vasily@pupkin.com").build())
                .quickPay(new QuickPay.Builder(
                        catalogItems.get(0).getName(),
                        new Money.Builder()
                                .amount(catalogItems.get(0).getVariations().get(0).getItemVariationData().getPriceMoney().getAmount())
                                .currency("CAD")
                                .build(),
                        "LVA3770KXBP3P"
                ).build()).build();

        try {
            CreatePaymentLinkResponse paymentLink = checkoutApi.createPaymentLinkAsync(body).get();
            System.out.println("Success!");
            System.out.println(paymentLink);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to make the request");
            System.out.println(String.format("Exception: %s", e.getMessage()));
        }
    }

}
