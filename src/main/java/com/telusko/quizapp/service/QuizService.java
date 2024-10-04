package com.telusko.quizapp.service;

import com.telusko.quizapp.dao.LeaderboardDao;
import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.dao.QuizDao;
import com.telusko.quizapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quiz;

    @Autowired
    QuestionDao questionDao;
    @Autowired
    private QuizDao quizDao;
    @Autowired
    private LeaderboardDao leaderboardDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category,numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    public ResponseEntity<List<QuestionWrapper>>getQuizQuestions(Integer id) {
       Optional<Quiz> quiz= quizDao.findById(id);
       List<Question> questionsFromDB = quiz.get().getQuestions();
       List<QuestionWrapper> questionForUser = new ArrayList<>();
       for(Question q : questionsFromDB) {
           QuestionWrapper qw =  new QuestionWrapper(q.getOption2(),q.getOption3(),q.getOption4(),q.getOption1(),q.getQuestionTitle(),q.getId());
           questionForUser.add(qw);
       }

       return new ResponseEntity<>(questionForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id,Integer userId, List<Response> responses) {
        Quiz quiz =  quizDao.findById(id).get();
        Leaderboard leaderboard = new Leaderboard();

        List<Question> questions = quiz.getQuestions();
        int right =0;
        int i =0;

        for (Response response :responses){
            if(response.getResponse().equals(questions.get(i).getRightAnswer()))
                right++;
            i++;
        }
        leaderboard.setUserId(userId);
        leaderboard.setQuizId(id);
        leaderboard.setScore(right);
        leaderboard.setAttemptNo(attemptFinder(id,userId));
        leaderboardDao.save(leaderboard);
        return new ResponseEntity<>(right,HttpStatus.OK);
    }

    public ResponseEntity<List<Leaderboard>> getLeaderboard(){
        List<Leaderboard> leaderboards = leaderboardDao.getLeaderboard();
        return new ResponseEntity<>(leaderboards,HttpStatus.OK);
    }

    public int attemptFinder(Integer id,Integer userId){
        Optional<Integer> attemptNumber = leaderboardDao.getAttemptNumber(id,userId);
        if(attemptNumber.isPresent()){
            attemptNumber=Optional.of(attemptNumber.get() + 1);
        }
        else{
           attemptNumber= Optional.of(1);
        }
    return attemptNumber.get();
    }
}
