package com.oriontech.managementsystem.core.utils;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppResponse {
    private @NotNull EProcessStatus status;
    private @NotNull String message;
    private Object response;
}
