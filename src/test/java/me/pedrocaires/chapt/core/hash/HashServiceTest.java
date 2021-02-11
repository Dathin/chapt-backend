package me.pedrocaires.chapt.core.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HashServiceTest {

    @Test
    void shouldSha256Pedro() {
        String pedro = "pedro";
        String pedroHashed = "7lzX1dlsiHQReJGyySoDb5aRjmbBArxpiud1QsGG+YE=";
        byte base64BytesCount = 44;

        String hash = HashService.getHash(pedro);

        assertEquals(base64BytesCount, hash.getBytes().length);
        assertEquals(pedroHashed, hash);
    }

    @Test
    void shouldSha256PedroWithSalt() {
        String pedro = "pedro";
        String salt = "salt";
        String pedroHashedWithSalt = "ctuISIbc0vlnKo6Ug2tTMrdKrwNcT/1m5M9ABGM2pHk=";
        String pedroHashedWithoutSalt = "7lzX1dlsiHQReJGyySoDb5aRjmbBArxpiud1QsGG+YE=";
        short base64BytesCount = 44;

        String hash = HashService.getHash(pedro, salt);

        assertEquals(base64BytesCount, hash.getBytes().length);
        assertEquals(pedroHashedWithSalt, hash);
        assertNotEquals(pedroHashedWithoutSalt, hash);
    }

    @Test
    void shouldReturnRandomUuidForSalting() {
        byte uuidLength = 36;

        String salt = HashService.getSalt();

        assertEquals(uuidLength, salt.length());
    }

}
