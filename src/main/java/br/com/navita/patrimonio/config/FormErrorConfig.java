package br.com.navita.patrimonio.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RestControllerAdvice
public class FormErrorConfig {

	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, List<FormError>> handle(final MethodArgumentNotValidException exception) {
		final Map<String, List<FormError>> errors = new HashMap<>();
		errors.put("errors", exception.getBindingResult().getFieldErrors().stream()
			.map(e -> new FormError(e.getField(), this.messageSource.getMessage(e, LocaleContextHolder.getLocale())))
			.collect(Collectors.toList())
		);
		return errors;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	class FormError {

		private final String field;
		private final String error;
	}
}