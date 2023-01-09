package com.test.action.entity.statistic;

import com.test.action.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.util.UUID;

@ToString
@Builder
@AllArgsConstructor
public class UserStatistic implements Statistic {
    private UUID userId;
    private String name;
    private String channel;
    private Long count;
    
    public UserStatistic(User user) {
        this(user, 0L);
    }
    
    public UserStatistic(User user, Long count) {
        this.userId = user != null ? user.getId() : null;
        this.name = user != null ? user.resolveFirstnameLastname() : null;
        this.channel = user != null && user.hasFirstnameLastname() ? user.resolveChannel() : null;
        this.count = count;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getKey() {
        return userId != null ? userId.toString() : null;
    }
    
    @Override
    public Integer getCount() {
        return count.intValue();
    }
    
    @Override
    public String getAdditionalInfo() {
        return channel;
    }
}
