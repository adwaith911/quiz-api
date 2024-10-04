package com.telusko.quizapp.dao;

import com.telusko.quizapp.model.Leaderboard;
import com.telusko.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardDao extends JpaRepository<Leaderboard,Integer> {

    @Query(value= "SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10;", nativeQuery=true)
    List<Leaderboard> getLeaderboard();

    @Query(value= "select attempt_no from(SELECT * FROM leaderboard WHERE user_id =:userId AND quiz_id =:id ORDER BY attempt_no DESC LIMIT 1) sub;", nativeQuery=true)
    Optional<Integer> getAttemptNumber(int id,int userId);
}
