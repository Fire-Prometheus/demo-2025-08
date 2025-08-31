package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "ACCOUNT")
public class AccountEntity {
    @Id
    @Column(name = "iban", nullable = false)
    private String iban;

    @Column(name = "user_id")
    private String userId;

    @Version
    @Column(name = "version")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer version;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by", length = 20)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 20)
    private String lastModifiedBy;
}
