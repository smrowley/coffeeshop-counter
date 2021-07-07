package com.delta.coffeeshop.counter.ddb;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.delta.coffeeshop.counter.domain.Order;
import com.delta.coffeeshop.counter.domain.dao.DynamoDBDao;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class DynamoDBDaoTest {
    private static final String ID = "xzci94kdm39fb58dfeonsoos22394";
    @Inject
    DynamoDBDao ddbDao;

    @InjectMock
    DynamoDBMapper ddbMapper;
    Order order = new Order(ID);

    @Test
    public void testPersist() {
        ddbDao.persist(order);
        verify(ddbMapper, times(1)).save(any(Order.class));
    }

    @Test
    public void testFindById() {
        when(ddbMapper.load(Order.class, ID)).thenReturn(order);
        ddbDao.findById(ID);
        verify(ddbMapper, times(1)).load(Order.class, ID);
    }

    @Test
    public void testFindByIdNullOrderId() {
        assertThrows(RuntimeException.class, () -> {
            ddbDao.findById(null);
        });
    }
}
