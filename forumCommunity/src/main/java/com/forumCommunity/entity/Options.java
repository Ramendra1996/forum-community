package com.forumCommunity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionsId;
    private Long pollId;
    private String value;

    private Date dateOfCreation;
    private Date dateOfModification;
}
