package com.forumCommunity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopGainerItem {
    private String symbol;
    private Double ltp;
    private Double priceChangePct;
    private Double oiChangePct;
}
