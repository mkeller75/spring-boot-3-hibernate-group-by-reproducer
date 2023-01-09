package com.test.action.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Joiner.on;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "ACT_USER")
@NoArgsConstructor
public class User extends BaseEntity {
    
    @Column(name = "PARTNER_NUMBER", length = 50)
    String partnerNumber;
    
    @Column(name = "FIRSTNAME", length = 40)
    private String firstname;
    
    @Column(name = "LASTNAME", length = 40)
    private String lastname;
    
    @Column(name = "EMAIL", length = 128)
    private String email;
    
    @Column(name = "MOBILE", length = 30)
    private String mobile;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Action> actions = new ArrayList<>();
    
    public boolean hasFirstnameLastname() {
        return isNotBlank(resolveFirstnameLastname());
    }
    
    public String resolveFirstnameLastname() {
        return on(" ").skipNulls().join(firstname, lastname);
    }
    
    public String resolveChannel() {
        if (mobile == null) {
            return email;
        } else {
            return mobile;
        }
    }
}
