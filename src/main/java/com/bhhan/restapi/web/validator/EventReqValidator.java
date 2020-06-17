package com.bhhan.restapi.web.validator;

import com.bhhan.restapi.service.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

/**
 * Created by hbh5274@gmail.com on 2020-06-15
 * Github : http://github.com/bhhan5274
 */

@Component
public class EventReqValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EventDto.EventReq.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventDto.EventReq eventReq = (EventDto.EventReq) target;

        if(eventReq.getBasePrice() > eventReq.getMaxPrice() && eventReq.getMaxPrice() > 0){
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong.");
            errors.reject("wrongPrices", "Values for prices are wrong.");
        }

        LocalDateTime endEventDateTime = eventReq.getEndEventDateTime();

        if(endEventDateTime.isBefore(eventReq.getBeginEventDateTime())
                || endEventDateTime.isBefore(eventReq.getCloseEnrollmentDateTime())
                || endEventDateTime.isBefore(eventReq.getBeginEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong.");
        }

        // TODO: Another Validation Login
    }
}
