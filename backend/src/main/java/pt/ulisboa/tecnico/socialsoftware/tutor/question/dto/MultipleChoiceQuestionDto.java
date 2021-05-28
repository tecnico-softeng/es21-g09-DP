package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleChoiceQuestionDto extends QuestionDetailsDto {
    private boolean ordered = false;
    private List<OptionDto> options = new ArrayList<>();

    public MultipleChoiceQuestionDto() {
    }

    public MultipleChoiceQuestionDto(MultipleChoiceQuestion question) {
        this.ordered = question.isOrdered();
        this.options = question.getOptions().stream()
                    .map(OptionDto::new)
                    .collect(Collectors.toList());
        
        this.options.sort(Comparator.comparing(OptionDto::getId, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    public boolean isOrdered(){
        return ordered;
    }

    public void setOrdered(boolean ordered){
        this.ordered = ordered;
    }

    public List<OptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDto> options) {
        this.options = options;
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new MultipleChoiceQuestion(question, this);
    }

    @Override
    public void update(MultipleChoiceQuestion question) {
        question.update(this);
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestionDto{" +
                "ordered=" + ordered +
                "options=" + options +
                '}';
    }

}
