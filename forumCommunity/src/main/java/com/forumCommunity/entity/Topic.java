package com.forumCommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;
    private Long  categoryId;
    private  String topicName;
    private Date dateOfCreation;
    private Date dateOfModification;
    @Transient
    private String categoryName;

    private Long postCount;



   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;
    private Long  categoryId=1L;
    private  String topicName;
    private Date dateOfCreation=new Date();
    private Date dateOfModification=new Date();*/

}
