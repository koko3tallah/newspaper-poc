package com.kerolos.newspaper.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditedEntity implements Serializable {

    @CreatedBy
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
