package com.delta.coffeeshop.counter.ddb;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider.Builder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBFactory;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DynamoDBFactoryTest {
    @Inject
    DynamoDBFactory dynamDBFactory;
    AmazonDynamoDB amazonDynamoDB;
    AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder;
    WebIdentityTokenCredentialsProvider webIdentityTokenCredentialsProvider;
    Builder webBuilder;

    @BeforeEach
    public void setup() {
        amazonDynamoDB = Mockito.mock(AmazonDynamoDB.class);
        amazonDynamoDBClientBuilder = Mockito.mock(AmazonDynamoDBClientBuilder.class);
        webIdentityTokenCredentialsProvider =
                Mockito.mock(WebIdentityTokenCredentialsProvider.class);
        webBuilder = Mockito.mock(Builder.class);
    }

    @Test
    public void testGetDdbMapper() {

        try (MockedStatic<AmazonDynamoDBClientBuilder> amazonDynamoDBClientBuilderMock =
                Mockito.mockStatic(AmazonDynamoDBClientBuilder.class);
                MockedStatic<WebIdentityTokenCredentialsProvider> webIdentityTokenCredentialsProviderMock =
                        Mockito.mockStatic(WebIdentityTokenCredentialsProvider.class);) {
            webIdentityTokenCredentialsProviderMock
                    .when(() -> WebIdentityTokenCredentialsProvider.builder())
                    .thenReturn(webBuilder);
            webIdentityTokenCredentialsProviderMock
                    .when(() -> WebIdentityTokenCredentialsProvider.builder().build())
                    .thenReturn(webIdentityTokenCredentialsProvider);
            amazonDynamoDBClientBuilderMock.when(() -> AmazonDynamoDBClientBuilder.standard())
                    .thenReturn(amazonDynamoDBClientBuilder);
            amazonDynamoDBClientBuilderMock
                    .when(() -> AmazonDynamoDBClientBuilder.standard()
                            .withCredentials(webIdentityTokenCredentialsProvider))
                    .thenReturn(amazonDynamoDBClientBuilder);
            amazonDynamoDBClientBuilderMock.when(() -> AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(webIdentityTokenCredentialsProvider)
                    .withRegion(Regions.US_EAST_1)).thenReturn(amazonDynamoDBClientBuilder);
            amazonDynamoDBClientBuilderMock.when(() -> AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(webIdentityTokenCredentialsProvider)
                    .withRegion(Regions.US_EAST_1).build()).thenReturn(amazonDynamoDB);
            assertNotNull(dynamDBFactory.createDDBMapperInstance());
        }
    }
}
