package com.delta.coffeeshop.counter.domain.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.delta.coffeeshop.counter.domain.Order;

@ApplicationScoped
public class DynamoDBDao {
    static Logger logger = LoggerFactory.getLogger(Order.class);
    @Inject
    DynamoDBMapper ddbMapper;

    public Order findById(final String orderId) {
        Order order = ddbMapper.load(Order.class, orderId);
        if (order == null) {
            throw new RuntimeException("Order with ID " + orderId + " not found ");
        }
        logger.debug("Order with ID {} found", order.getOrderId());
        return order;
    }

    public Order persist(Order order) {
        ddbMapper.save(order);
        logger.debug("Order with ID {} saved", order.getOrderId());
        return order;
    }

}
