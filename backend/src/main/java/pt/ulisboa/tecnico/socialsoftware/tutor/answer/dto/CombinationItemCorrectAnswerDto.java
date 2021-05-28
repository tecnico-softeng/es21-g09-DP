package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion;

public class CombinationItemCorrectAnswerDto extends CorrectAnswerDetailsDto{
    private List<CombinationItemAnswerOptionDto> correctOptions;

    public CombinationItemCorrectAnswerDto(CombinationItemQuestion question) {
        this.correctOptions = question.getOptions()
                    .stream()
                    .map(CombinationItemAnswerOptionDto::new)
                    .collect(Collectors.toList());
    }

    public List<CombinationItemAnswerOptionDto> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptionId(List<CombinationItemAnswerOptionDto> correctOptions) {
        this.correctOptions = correctOptions;
    }

    @Override
    public String toString() {
        return "CombinationItemCorrectAnswerDto{" +
                "correctOptions=" + correctOptions +
                '}';
    }
    
}
