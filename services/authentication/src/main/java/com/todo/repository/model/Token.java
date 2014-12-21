package com.todo.repository.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String value;

    @Column(nullable = false)
    private Long lastUsage;

    @ManyToOne(optional = false)
    private User user;

    public Token(Long lastUsage, User user) {
        this.lastUsage = lastUsage;
        this.user = user;
    }

    private Token() {
    }

    public String getValue() {
        return value;
    }

    public Long getLastUsage() {
        return lastUsage;
    }

    public User getUser() {
        return user;
    }

    public void setLastUsage(Long lastUsage) {
        this.lastUsage = lastUsage;
    }
}
