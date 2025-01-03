package com.devsuperior.dsmeta.projections;

import java.time.LocalDate;

public interface ReportMinProjection {
    Long getId();
    LocalDate getDate();
    Double getAmount();
    String getSellerName();
}
