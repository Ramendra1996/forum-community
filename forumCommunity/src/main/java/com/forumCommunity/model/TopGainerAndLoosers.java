package com.forumCommunity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopGainerAndLoosers {
    private List<Item> topGainers;
    private List<Item> topLosers;
}
