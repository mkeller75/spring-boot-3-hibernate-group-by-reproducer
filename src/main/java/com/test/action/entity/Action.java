package com.test.action.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "ACT_ACTION")
@NoArgsConstructor
public class Action extends BaseEntity {
    
    @Column(name = "PARTNER_NUMBER", length = 50)
    String partnerNumber;
    
    @Column(name = "TITLE", nullable = false)
    private String title;
    
    @Column(name = "FINDING")
    private String finding;
    
    @Column(name = "DEADLINE")
    @Setter
    private LocalDate deadline;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "FK_USER_ID")
    @Setter
    private User user;
}
