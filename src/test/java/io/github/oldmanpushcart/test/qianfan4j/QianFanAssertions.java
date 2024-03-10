package io.github.oldmanpushcart.test.qianfan4j;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;
import org.junit.jupiter.api.Assertions;

public class QianFanAssertions {

    public static void assertApiResponse(ApiResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.ret());
        Assertions.assertTrue(response.ret().isSuccess());
        Assertions.assertNotNull(response.uuid());
        Assertions.assertFalse(response.uuid().isBlank());
    }

    public static void assertAlgoResponse(AlgoResponse response) {
        assertApiResponse(response);
        Assertions.assertNotNull(response.usage());
        Assertions.assertTrue(response.usage().total() >= 0);
    }

}
