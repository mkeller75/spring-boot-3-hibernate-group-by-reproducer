package com.test.action.persistence;

import com.test.action.entity.Action;
import com.test.action.entity.User;
import com.test.action.entity.statistic.Statistic;
import com.test.action.entity.statistic.UserStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ActionRepositoryTest {
    private static final String PARTNER_NUMBER = "1111111111";
    
    @Autowired
    TestEntityManager entityManager;
    
    @Autowired
    ActionRepository actionRepository;
    
    @Nested
    class StatisticsTest {
        UUID userId1;
        String userName1;
        
        UUID userId2;
        String userName2;
        
        UUID userId3;
        String userName3;
        
        @BeforeEach
        void setup() {
            final var user1 = User.builder().id(UUID.randomUUID()).partnerNumber(PARTNER_NUMBER).firstname("John").lastname("Tester").email("john.tester@test.com").build();
            final var user2 = User.builder().id(UUID.randomUUID()).partnerNumber(PARTNER_NUMBER).firstname("Chris").lastname("Tester").email("chris.tester@test.com").build();
            final var user3 = User.builder().id(UUID.randomUUID()).partnerNumber(PARTNER_NUMBER).firstname("Anna").lastname("Tester").email("anna.tester@test.com").build();
            
            userId1 = user1.getId();
            userName1 = user1.resolveFirstnameLastname();
            
            userId2 = user2.getId();
            userName2 = user2.resolveFirstnameLastname();
            
            userId3 = user3.getId();
            userName3 = user3.resolveFirstnameLastname();
            
            final var deadline = OffsetDateTime.now().toLocalDate().plusDays(60);
            final var action1 = Action.builder().partnerNumber(PARTNER_NUMBER).title("Test 1").finding("Finding 1").deadline(deadline).user(user1).build();
            final var action2 = Action.builder().partnerNumber(PARTNER_NUMBER).title("Test 2").finding("Finding 2").deadline(deadline).user(user2).build();
            final var action3 = Action.builder().partnerNumber(PARTNER_NUMBER).title("Test 3").finding("Finding 3").deadline(deadline).user(user3).build();
            final var action4 = Action.builder().partnerNumber(PARTNER_NUMBER).title("Test 4").finding("Finding 4").deadline(deadline).user(user3).build();
            final var action5 = Action.builder().partnerNumber(PARTNER_NUMBER).title("Test 5").finding("Finding 5").deadline(deadline).user(null).build();
            
            entityManager.persist(user1);
            entityManager.persist(user2);
            entityManager.persist(user3);
            
            entityManager.persist(action1);
            entityManager.persist(action2);
            entityManager.persist(action3);
            entityManager.persist(action4);
            entityManager.persist(action5);
        }
        
        @Test
        void getUserStatistic_returnStatisticsOfAllUserIds() {
            // when
            List<UserStatistic> userStatistic = actionRepository.getUserStatistic(PARTNER_NUMBER);
            
            // then
            assertThat(userStatistic).hasSize(3);
            
            Statistic stat1 = getStatisticByName(userStatistic, userName1);
            assertStatistic(stat1, userName1, userId1.toString(), 1);
            
            Statistic stat2 = getStatisticByName(userStatistic, userName2);
            assertStatistic(stat2, userName2, userId2.toString(), 1);
            
            Statistic stat3 = getStatisticByName(userStatistic, userName3);
            assertStatistic(stat3, userName3, userId3.toString(), 2);
        }
        
        private Statistic getStatisticByName(List<? extends Statistic> statistics, String name) {
            Optional<? extends Statistic> statistic = statistics.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst();
            assertThat(statistic).isPresent();
            
            return statistic.get();
        }
        
        private void assertStatistic(Statistic statistic, String name, String key, int count) {
            assertThat(statistic)
                .extracting(Statistic::getName, Statistic::getKey, Statistic::getCount)
                .containsExactly(name, key, count);
        }
    }
}
