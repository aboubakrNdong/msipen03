package com.cl.msipen03.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.List;

@Builder
public record RemittanceRequestPaginationResponse(
        int size,
        int page,
        long total,

        @JsonProperty("Synthese_Operations")
        RemittanceRequest request,

        @JsonProperty("Mes_Virements")
        List<RemittanceOperations> list

) {
}