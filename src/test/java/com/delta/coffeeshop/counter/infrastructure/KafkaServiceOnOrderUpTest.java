package com.delta.coffeeshop.counter.infrastructure;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.delta.coffeeshop.counter.domain.Order;
import com.delta.coffeeshop.counter.domain.OrderStatus;
import com.delta.coffeeshop.counter.domain.commands.CommandItem;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBDao;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderEventResult;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderUpdate;
import com.delta.coffeeshop.counter.domain.valueobjects.TicketUp;
import com.delta.coffeeshop.infrastructure.KafkaService;
import com.delta.coffeeshop.testing.TestUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;

@QuarkusTest
@Transactional
public class KafkaServiceOnOrderUpTest {

    @ConfigProperty(name = "mp.messaging.incoming.orders-up.topic")
    protected String ORDERS_UP;

    @ConfigProperty(name = "mp.messaging.outgoing.web-updates.topic")
    protected String WEB_UPDATES;

    @Inject
    KafkaService kafkaService;
    @InjectMock
    DynamoDBDao orderRepository;

    @Inject
    @Any
    InMemoryConnector connector;

    InMemorySource<TicketUp> ordersUp;

    InMemorySink<Object> webUpdatesSink;

    @BeforeEach
    public void setUp() {
        ordersUp = connector.source(ORDERS_UP);
        webUpdatesSink = connector.sink(WEB_UPDATES);
        webUpdatesSink.clear();
    }

    /**
     * Verify that the appropriate method is called on OrderService when a TicketUp is received
     * 
     * @see TicketUp
     * @see PlaceOrderCommand
     *
     */
    @Test
    public void testOrderUp() {
        // Place order so that order exists in database when it's processed
        PlaceOrderCommand placeOrderCommand = TestUtil.stubPlaceOrderCommand();
        OrderEventResult orderEventResult = Order.process(placeOrderCommand);

        // orderRepository.persist(orderEventResult.getOrder());

        String orderId = placeOrderCommand.getId();
        TicketUp orderTicketUp = TestUtil.stubOrderTicketUp(orderId);
        Mockito.when(orderRepository.findById(orderId)).thenReturn(orderEventResult.getOrder());
        kafkaService.orderUp(orderTicketUp);
        // Expect one web update message in web_updates topic
        assertThat(webUpdatesSink.received().size(), equalTo(1));
        // Expected OrderUpdate[orderId='guid', itemId='guid', name='Capt. Kirk',
        // item=COFFEE_BLACK, status=FULFILLED, madeBy='Mr. Spock']
        Message<Object> message = webUpdatesSink.received().get(0);
        assertTrue(message.getPayload() instanceof OrderUpdate);
        OrderUpdate orderUpdate = (OrderUpdate) message.getPayload();
        assertThat(orderUpdate.getOrderId(), equalTo(orderId));
        CommandItem baristaItem = placeOrderCommand.getBaristaLineItems().get().get(0);
        assertThat(orderUpdate.getItem(), equalTo(baristaItem.getItem()));
        assertThat(orderUpdate.getName(), equalTo("Capt. Kirk"));
        assertThat(orderUpdate.getStatus(), equalTo(OrderStatus.FULFILLED));
        assertThat(orderUpdate.getMadeBy().get(), equalTo("Mr. Spock"));
    }
}
