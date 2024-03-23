package it.unibs.pajc.lithium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountManagerTest {
    @Test
    void getLoginInfo() {
        AccountManager.saveLoginInfo("hello", "world");
        var loginInfo = AccountManager.getLoginInfo();
        assertNotNull(loginInfo);
        assertEquals("hello", loginInfo.username());
        assertEquals("world", loginInfo.passwordHash());
    }
}