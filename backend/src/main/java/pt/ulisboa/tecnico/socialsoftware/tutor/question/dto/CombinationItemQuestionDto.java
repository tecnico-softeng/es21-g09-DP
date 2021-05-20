package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CombinationItemQuestionDto extends QuestionDetailsDto {
    private List<CombOptionDto> options = new ArrayList<CombOptionDto>();

    public CombinationItemQuestionDto() { }

    public CombinationItemQuestionDto(CombinationItemQuestion question) {
        this.options = question.getOptions().stream().map(CombOptionDto::new).collect(Collectors.toList());
    }

    public List<CombOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<CombOptionDto> options) {
        this.options = options;
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new CombinationItemQuestion(question, this);
    }

    @Override
    public void update(CombinationItemQuestion question) {
        question.update(this);
    }

    @Override
    public String toString() {
        return "CombinationItemQuestionDto{Options: "+ options +'}';
    }
}