package com.sync;

import com.sync.client.XubioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CobranzaTimeoutTest {

    @Autowired
    private XubioClient xubioClient;

    @Test
    public void testCobranza() {
        System.out.println("Starting feign call...");
        String token = "Bearer 2H9ROh=c1Ay6AFxEEIm*W_i+r98hxI3KI=wEYrBZAmUjyBhM0mCtaNwY7ge+P9PYoNfAEevnwZfb8-5SX0gX*qBp5v030XzZ/Ngz2iYGukfnG9tocU8WSSOnSDu32H9ROh=c1Ay6AFxEEIm*W_i+r9";
        try {
            var res = xubioClient.getCobranzas(token, "2026-04-28", "2026-04-28");
            System.out.println("SUCCESS: " + res.size());
        } catch (Exception e) {
            System.out.println("TEST_EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
