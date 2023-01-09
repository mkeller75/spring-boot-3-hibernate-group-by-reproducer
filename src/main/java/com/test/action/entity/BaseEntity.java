package com.test.action.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@MappedSuperclass
@ToString(onlyExplicitlyIncluded = true)
public class BaseEntity {
    @Id
    @Column(name = "ID")
    @Basic
    @JdbcTypeCode(SqlTypes.CHAR)
    @ToString.Include
    private UUID id;
    
    @Column(name = "SEQUENCE_NUMBER", nullable = false)
    @Version
    private Long sequenceNumber;
    
    @Column(name = "MODIFICATION_TIMESTAMP", nullable = false)
    private OffsetDateTime modificationTimestamp;
    
    @Column(name = "CREATION_TIMESTAMP", nullable = false)
    private OffsetDateTime creationTimestamp;
    
    @PreUpdate
    public void preUpdate() {
        modificationTimestamp = OffsetDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        if (isNull(this.id)) {
            this.id = UUID.randomUUID();
        }
        if (sequenceNumber == null) {
            sequenceNumber = 0L;
        }
        if (creationTimestamp == null) {
            creationTimestamp = OffsetDateTime.now();
        }
        preUpdate();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

