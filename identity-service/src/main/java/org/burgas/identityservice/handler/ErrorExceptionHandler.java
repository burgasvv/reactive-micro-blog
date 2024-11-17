package org.burgas.identityservice.handler;

import lombok.Getter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.*;
import static org.springframework.boot.web.error.ErrorAttributeOptions.of;
import static org.springframework.http.HttpStatus.MULTI_STATUS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Getter
@Component
public class ErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ObjectProvider<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public ErrorExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties.Resources resources, ApplicationContext applicationContext,
            ObjectProvider<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, resources, applicationContext);
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
        super.setViewResolvers(viewResolvers.stream().toList());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(), request ->
                    ServerResponse.status(MULTI_STATUS)
                            .contentType(APPLICATION_JSON)
                            .body(fromValue(errorAttributes.getErrorAttributes(
                                            request, of(MESSAGE, ERROR, EXCEPTION, BINDING_ERRORS, PATH, STACK_TRACE, STATUS))
                                    )
                            )
        );
    }
}
