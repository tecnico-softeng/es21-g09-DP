package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
public class MultipleChoiceAnswerDto extends AnswerDetailsDto {
    private List<MultipleChoiceAnswerOptionDto> answeredOptions = new ArrayList<>();

    public MultipleChoiceAnswerDto() {
    }

    public MultipleChoiceAnswerDto(MultipleChoiceAnswer answer) {
        if (answer.getAnsweredOptions() != null){
            this.answeredOptions = answer.getAnsweredOptions().stream().map(MultipleChoiceAnswerOptionDto::new).collect(Collectors.toList());
            this.answeredOptions.sort(Comparator.comparing(MultipleChoiceAnswerOptionDto::getOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                                                .thenComparing(MultipleChoiceAnswerOptionDto::getOptionId));
        }
    }

    public List<MultipleChoiceAnswerOptionDto> getAnsweredOptions(){
        return answeredOptions;
    }

    public void setAnsweredOptions(List<MultipleChoiceAnswerOptionDto> answeredOptions) {
        this.answeredOptions = answeredOptions;
    }
}
