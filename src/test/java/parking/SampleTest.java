package parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void walletBasics() {
        Wallet w = new Wallet(100.0);
        w.addFunds(50.0);
        assertTrue(w.getBalance() > 0);
        w.deductFunds(30.0);
        assertTrue(w.getBalance() > 0);
    }
}
