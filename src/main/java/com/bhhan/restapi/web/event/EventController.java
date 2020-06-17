package com.bhhan.restapi.web.event;

import com.bhhan.restapi.domain.Account;
import com.bhhan.restapi.domain.Event;
import com.bhhan.restapi.domain.EventRepository;
import com.bhhan.restapi.service.dto.CurrentUser;
import com.bhhan.restapi.service.dto.EventDto;
import com.bhhan.restapi.web.dto.ErrorsResource;
import com.bhhan.restapi.web.dto.EventResource;
import com.bhhan.restapi.web.validator.EventReqValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Created by hbh5274@gmail.com on 2020-06-15
 * Github : http://github.com/bhhan5274
 */

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final EventReqValidator eventReqValidator;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(eventReqValidator);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity queryEvent(@PathVariable Long eventId, @CurrentUser Account account){
        final Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        final Event event = optionalEvent.get();
        final EventResource eventResource = new EventResource(event);

        if(Objects.nonNull(account) && account.getId().equals(event.getId())){
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity updateEvent(@CurrentUser Account account, @PathVariable Long eventId, @RequestBody @Valid EventDto.EventReq eventReq, Errors errors) {
        final Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        if(Objects.isNull(account) || !optionalEvent.get().getId().equals(account.getId())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        final Event event = optionalEvent.get();
        event.updateEvent(eventReq);
        final Event savedEvent = eventRepository.save(event);

        return ResponseEntity.ok(new EventResource(savedEvent));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler,
                                      @CurrentUser Account account){
        final Page<Event> page = this.eventRepository.findAll(pageable);
        final PagedModel<EventResource> pageResources = assembler.toModel(page, EventResource::new);
        pageResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        if(Objects.nonNull(account)){
            pageResources.add(linkTo(EventController.class).withRel("create-event"));
        }

        return ResponseEntity.ok(pageResources);
    }

    @PostMapping
    public ResponseEntity createEvent(@CurrentUser Account account, @RequestBody @Valid EventDto.EventReq eventReq, Errors errors){

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        final Event savedEvent = eventRepository.save(eventReq.toEvent(account));
        final WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(savedEvent.getId());
        final URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(EventResource.of(savedEvent));
    }
}
