package com.bhhan.restapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-06-15
 * Github : http://github.com/bhhan5274
 */
class EventTest {
    @DisplayName("Event Builder Test")
    @Test
    void event_builder(){
        final Event event = Event.builder().build();
        assertNotNull(event);
    }

    @DisplayName("Event Bean Test")
    @Test
    void event_bean(){
        final Event event = new Event();
        assertNotNull(event);
    }

    @DisplayName("")
    @Test
    void testFree(){
        final Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        assertEquals(true, event.isFree());
    }
}