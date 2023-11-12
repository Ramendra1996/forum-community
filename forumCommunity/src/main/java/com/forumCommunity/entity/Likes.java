package com.forumCommunity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likesId;
    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "post_id",nullable = false)
    private  Long postId;
    private Date dateOfCreation;
    private Date dateOfModification;
}
