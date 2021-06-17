package com.delta.coffeeshop.counter.infrastructure;

import com.delta.coffeeshop.counter.domain.OrderStatus;
import com.delta.coffeeshop.counter.domain.commands.CommandItem;
import com.delta.coffeeshop.counter.domain.commands.PlaceOrderCommand;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderTicket;
import com.delta.coffeeshop.counter.domain.valueobjects.OrderUpdate;
import com.delta.coffeeshop.infrastructure.KafkaService;
import com.delta.coffeeshop.infrastructure.OrderService;
import com.delta.coffeeshop.testing.TestUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@Transactional
public class KafkaServiceOnOrderInTest {

    @ConfigProperty(name = "mp.messaging.incoming.orders-in.topic")
    protected String ORDERS_IN;

    protected String BARISTA = "barista";

    @ConfigProperty(name = "mp.messaging.outgoing.web-updates.topic")
    protected String WEB_UPDATES;

    @Inject
    KafkaService kafkaService;

    @Inject
    @Any
    InMemoryConnector connector;

    InMemorySource<PlaceOrderCommand> ordersIn;

    @BeforeEach
    public void setUp() {
        ordersIn = connector.source(ORDERS_IN);
    }

    /**
     * Verify that the appropriate method is called on OrderService when a PlaceOrderCommand is received
     *
     * @see OrderService
     * @see PlaceOrderCommand
     */
    @Test
    public void testOrderIn() {
        PlaceOrderCommand placeOrderCommand = TestUtil.stubPlaceOrderCommand();
        kafkaService.orderIn(placeOrderCommand);

        InMemorySink<Object> webUpdatesSink = connector.sink(WEB_UPDATES);
        //Expect one web update message in web_updates topic
        assertThat(webUpdatesSink.received().size(), equalTo(1));
        //Expected Web update: OrderUpdate[orderId='guid', itemId='guid', name='Foo', item=COFFEE_BLACK, status=IN_PROGRESS, madeBy='null']
        Message<Object> message = webUpdatesSink.received().get(0);
        assertTrue(message.getPayload() instanceof OrderUpdate);
        OrderUpdate orderUpdate = (OrderUpdate) message.getPayload();
        assertThat(orderUpdate.getOrderId(), equalTo(orderUpdate.getOrderId()));
        CommandItem baristaItem = placeOrderCommand.getBaristaLineItems().get().get(0);
        assertThat(orderUpdate.getItem(), equalTo(baristaItem.getItem()));
        assertThat(orderUpdate.getName(), equalTo(baristaItem.getName()));
        assertThat(orderUpdate.getStatus(), equalTo(OrderStatus.IN_PROGRESS));

        InMemorySink<Object> baristaSink = connector.sink(BARISTA);
        //Expect one order ticket message in barista-in topic
        assertThat(baristaSink.received().size(), equalTo(1));
        //Expected Order Ticket update: OrderTicket{orderId='guid', id=guid, item=COFFEE_BLACK, name='Foo', timestamp=2021-06-11T19:12:25.660556400Z}
        Message<Object> baristaMsg = baristaSink.received().get(0);
        assertTrue(baristaMsg.getPayload() instanceof OrderTicket);
        OrderTicket orderTicket = (OrderTicket) baristaMsg.getPayload();
        assertThat(orderTicket.getOrderId(), equalTo(orderTicket.getOrderId()));
        assertThat(orderTicket.getItem(), equalTo(baristaItem.getItem()));
        assertThat(orderTicket.getName(), equalTo(baristaItem.getName()));
    }

}
