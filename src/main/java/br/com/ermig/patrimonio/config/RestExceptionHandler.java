package br.com.ermig.patrimonio.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenException(final Exception ex, final WebRequest request) {
    	final Map<String, String> mapErrors = Collections.singletonMap("error", ex.getMessage());
        return handleExceptionInternal(ex, mapErrors, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream().map(
			e -> new FieldError(e.getField(), this.messageSource.getMessage(e, LocaleContextHolder.getLocale()))
		).collect(Collectors.toList());
		final Map<String, List<FieldError>> mapErrors = Collections.singletonMap("errors", errors);
		return handleExceptionInternal(ex, mapErrors, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.error(ex.getMessage(), ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@Getter
	@RequiredArgsConstructor
	private class FieldError {

		private final String name;
		private final String error;
	}
}