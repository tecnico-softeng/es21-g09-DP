package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Languages;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenEndedQuestionDto extends QuestionDetailsDto {

    private String answer;

    public OpenEndedQuestionDto() {
    }

    public OpenEndedQuestionDto(OpenEndedQuestion question) {
        answer = question.getAnswer();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "OpenEndedQuestionDto{" +
                "answer=" + answer +
                '}';
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new OpenEndedQuestion(question, this);
    }

    @Override
    public void update(OpenEndedQuestion question) {
        question.update(this);
    }
}
