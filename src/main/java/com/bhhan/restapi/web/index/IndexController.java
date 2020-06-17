package com.bhhan.restapi.web.index;

import com.bhhan.restapi.web.dto.IndexResource;
import com.bhhan.restapi.web.event.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Created by hbh5274@gmail.com on 2020-06-16
 * Github : http://github.com/bhhan5274
 */

@RestController
public class IndexController {
    @GetMapping("/api")
    public RepresentationModel index(){
        final IndexResource index = new IndexResource();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
