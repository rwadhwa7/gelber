import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void customer() {
        Customer c = new Customer(7, 'A', 7, 7);
        assertEquals(7, c.id);
        assertEquals('A', c.type);
        assertEquals(7, c.arrivalTime);
        assertEquals(7, c.numItems);
    }

    @Test
    void customerWithNonPositiveNumItems() {
        try {
            Customer c = new Customer(7, 'A', 7, 0);
            assert (false);
        } catch (IllegalArgumentException iae) { assert(true); }

        try {
            Customer c = new Customer(7, 'A', 7, -1);
            assert (false);
        } catch (IllegalArgumentException iae) { assert(true); }
    }
}