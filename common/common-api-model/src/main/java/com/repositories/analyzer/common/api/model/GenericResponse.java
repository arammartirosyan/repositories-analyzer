package com.repositories.analyzer.common.api.model;

import com.repositories.analyzer.common.usecase.FailureDto;

import java.util.List;

public class GenericResponse extends AbstractResponse {

    private static final GenericResponse SUCCESS = new GenericResponse();

    private GenericResponse() {
        super();
    }

    public GenericResponse(final List<FailureDto> failures) {
        super(failures);
    }

    public static GenericResponse success() {
        return GenericResponse.SUCCESS;
    }
}