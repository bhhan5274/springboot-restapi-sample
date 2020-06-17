package com.bhhan.restapi.web.dto;

import com.bhhan.restapi.domain.Event;
import com.bhhan.restapi.web.event.EventController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Created by hbh5274@gmail.com on 2020-06-16
 * Github : http://github.com/bhhan5274
 */

@Getter
public class EventResource extends RepresentationModel<EventResource> {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event){
        this.event = event;
        final WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        add(selfLinkBuilder.withSelfRel(),
                linkTo(EventController.class).withRel("query-events"),
                selfLinkBuilder.withRel("update-event"),
                new Link("/docs/index.html#resources-events-create").withRel("profile"));
    }

    public static EventResource of(Event event){
        return new EventResource(event);
    }
}
