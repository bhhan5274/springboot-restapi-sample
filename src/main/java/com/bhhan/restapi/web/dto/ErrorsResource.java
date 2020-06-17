package com.bhhan.restapi.web.dto;

import com.bhhan.restapi.web.index.IndexController;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by hbh5274@gmail.com on 2020-06-16
 * Github : http://github.com/bhhan5274
 */

@Getter
public class ErrorsResource extends RepresentationModel<ErrorsResource> {
    private Errors errors;

    public ErrorsResource(Errors errors){
        this.errors = errors;
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
