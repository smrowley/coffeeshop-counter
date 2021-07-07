package com.delta.coffeeshop.counter.domain.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDBFactory {

    @Produces
    @ApplicationScoped
    public DynamoDBMapper createDDBMapperInstance() {

        return new DynamoDBMapper(
                AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1)
                        .withCredentials(WebIdentityTokenCredentialsProvider.builder().build())
                        .withRegion(Regions.US_EAST_1).build());

    }

}
