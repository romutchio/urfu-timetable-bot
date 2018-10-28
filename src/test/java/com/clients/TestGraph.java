package com.clients;

import com.server.GraphOfMessages;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGraph {
    public TestGraph() {
    }

    @Test
    public void testInitMessage() {
        var initMessage = GraphOfMessages.getInitMessage();
        assertEquals("Доброго времени суток!\n" +
                        "Я чат-бот, который поможет тебе не пропустить пары\n" +
                        "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
                initMessage.question);
    }

    @Test
    public void testTransitGetting() {
        try {
            var init = GraphOfMessages.getTransit("initialization");
            var repeat = GraphOfMessages.getTransit("repeat answer");
            assertNotNull(init);
            assertNotNull(repeat);
        } catch (Exception e) {
            fail();
        }
    }
}
