package com.techsophy.tstokens.exception;

import com.techsophy.tstokens.dto.common.ApiErrorsResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.utils.NullWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<IApiResponse> handleInvalidDataException(InvalidDataException rx, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).location(getLocation(request))
                .body(new ApiErrorsResponse(HttpStatus.UNPROCESSABLE_ENTITY, false, rx.getMessage()));
    }

    private URI getLocation(HttpServletRequest request) {
        Optional<Principal> principal = NullWrapper.resolve(request::getUserPrincipal);
        String userName = principal.isPresent() ? principal.get().getName() : "N/A";
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand(userName).toUri();

    }
}
