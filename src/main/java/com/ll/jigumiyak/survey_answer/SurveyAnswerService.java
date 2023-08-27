package com.ll.jigumiyak.survey_answer;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.survey.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyAnswerRepository;
    public SurveyAnswer createSurveyAnswer(String answerText, Survey survey) {
        SurveyAnswer surveyAnswer = SurveyAnswer.builder()
                .answerText(answerText)
                .survey(survey)
                .build();

        this.surveyAnswerRepository.save(surveyAnswer);
        return surveyAnswer;
    }

    public SurveyAnswer getSurveyAnswer(Long id) {
        Optional<SurveyAnswer> optionalSurveyAnswer = surveyAnswerRepository.findById(id);
        if (optionalSurveyAnswer.isPresent()){
            return optionalSurveyAnswer.get();
        } else {
            throw new DataNotFoundException("not found SurveyAnswer");
        }
    }
}
