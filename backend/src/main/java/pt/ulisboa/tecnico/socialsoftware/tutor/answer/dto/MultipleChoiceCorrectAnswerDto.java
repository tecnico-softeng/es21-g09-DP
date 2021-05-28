package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;

public class MultipleChoiceCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private List<MultipleChoiceAnswerOptionDto> correctOptions;

    public MultipleChoiceCorrectAnswerDto(MultipleChoiceQuestion question) {
        this.correctOptions = question.getOptions()
                    .stream()
                    .map(MultipleChoiceAnswerOptionDto::new)
                    .collect(Collectors.toList());
        this.correctOptions.sort(Comparator.comparing(MultipleChoiceAnswerOptionDto::getOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                                           .thenComparing(MultipleChoiceAnswerOptionDto::getOptionId));
    }

    public List<MultipleChoiceAnswerOptionDto> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptionId(List<MultipleChoiceAnswerOptionDto> correctOptions) {
        this.correctOptions = correctOptions;
    }

    @Override
    public String toString() {
        return "MultipleChoiceCorrectAnswerDto{" +
                "correctOptios=" + correctOptions +
                '}';
    }
}