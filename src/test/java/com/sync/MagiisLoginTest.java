package com.sync;

import com.sync.client.MagiisClient;
import com.sync.config.MagiisProperties;
import com.sync.dto.magiis.MagiisLoginRequest;
import com.sync.dto.magiis.MagiisLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class MagiisLoginTest {

    @Autowired
    private MagiisClient magiisClient;

    @Autowired
    private MagiisProperties magiisProperties;

    @Test
    public void testLogin() {
        MagiisLoginRequest req = new MagiisLoginRequest(
                magiisProperties.getUsername(),
                magiisProperties.getPassword()
        );
        MagiisLoginResponse res = magiisClient.login(req);
        System.out.println("TOKEN: " + res.getToken());
    }
}
