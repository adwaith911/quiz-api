package com.telusko.quizapp.controller;

import com.telusko.quizapp.model.Leaderboard;
import com.telusko.quizapp.model.Question;
import com.telusko.quizapp.model.QuestionWrapper;
import com.telusko.quizapp.model.Response;
import com.telusko.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category,@RequestParam int numQ,@RequestParam String title){
       return quizService.createQuiz(category,numQ,title);
    }
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);

    }

    @PostMapping("submit/{id}/{userId}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id,@PathVariable Integer userId, @RequestBody List<Response> responses){
        return quizService.calculateResult(id,userId,responses);
    }

    @GetMapping("get/leaderboard")
    public ResponseEntity<List<Leaderboard>> getLeaderboard(){
        return quizService.getLeaderboard();

    }

    
}
