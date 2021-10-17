package com.ahmedsr.task.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity(name = "Comment")
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false, updatable = false)
    @Getter
    private Long id;
    @JoinColumn(name = "post_id", nullable = false)
    @Setter
    @Getter
    private Long postId;
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    @Setter
    @Getter
    private String name;
    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    @Setter
    @Getter
    private String email;
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    @Setter
    @Getter
    private String body;

    public Comment(){

    }

    public Comment(Long postId, String name, String email, String body) {
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }

}
