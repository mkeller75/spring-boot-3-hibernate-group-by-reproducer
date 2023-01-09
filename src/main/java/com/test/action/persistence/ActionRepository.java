package com.test.action.persistence;

import com.test.action.entity.Action;
import com.test.action.entity.statistic.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {
    
    
    // Query does not work with spring boot 3
    @Query("SELECT new com.test.action.entity.statistic.UserStatistic(u, count(a))" +
        " FROM Action a INNER JOIN a.user u" +
        " WHERE a.partnerNumber = :partnerNumber" +
        " GROUP BY u")
    // Query working with spring boot 3
//    @Query("SELECT new com.test.action.entity.statistic.UserStatistic(u, count(a))" +
//        " FROM Action a INNER JOIN User u ON a.user.id = u.id"+
//        " WHERE a.partnerNumber = :partnerNumber" +
//        " GROUP BY u.id, u.sequenceNumber, u.modificationTimestamp, u.creationTimestamp, u.partnerNumber, u.firstname, u.lastname, u.email, u.mobile")
    List<UserStatistic> getUserStatistic(@Param("partnerNumber") String partnerNumber);
}
