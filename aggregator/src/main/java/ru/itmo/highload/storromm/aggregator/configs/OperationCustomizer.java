package ru.itmo.highload.storromm.aggregator.configs;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;

@Component
public class OperationCustomizer implements org.springdoc.core.customizers.OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        CustomizedOperation annotation = handlerMethod.getMethodAnnotation(CustomizedOperation.class);
        if (annotation != null) {
            operation.description(annotation.description());
            if(annotation.pageable()) {
                if(!containsParameter(operation, "page")) {
                    operation.addParametersItem(new Parameter().name("page").description("number of page"));
                }
                if(!containsParameter(operation, "size")) {
                    operation.addParametersItem(new Parameter().name("size").description("size of page"));
                }
                if(containsParameter(operation, "pageable")) {
                    int index = getParameterIndexByName(operation, "pageable");
                    operation.getParameters().remove(index);
                }
            }
            var r = operation.getResponses();
            for(int code : annotation.responseCodes()) {
                switch (code) {
                    case 400 ->
                            r.addApiResponse("400", new ApiResponse().description("Request is incorrect."));
                    case 401 ->
                            r.addApiResponse("401", new ApiResponse().description("You are not authorized to access the resource."));
                    case 403 ->
                            r.addApiResponse("403", new ApiResponse().description("You are forbidden to access the resource."));
                    case 404 ->
                            r.addApiResponse("404", new ApiResponse().description("Resource not found."));
                    case 409 ->
                            r.addApiResponse("409", new ApiResponse().description("Request action conflicts with the current state of things."));
                }
            }
        }
        return operation;
    }

    public static int getParameterIndexByName(Operation operation, String paramName) {
        if(operation.getParameters() == null) return -1;
        for(int i = 0; i < operation.getParameters().size(); i++) {
            if(operation.getParameters().get(i).getName().equals(paramName)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean containsParameter(Operation operation, String paramName) {
        return getParameterIndexByName(operation, paramName) > -1;
    }
}