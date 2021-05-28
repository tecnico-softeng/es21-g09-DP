package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.io.Serializable;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswerOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;

public class MultipleChoiceAnswerOptionDto implements Serializable{
    private Integer optionId;
    private Integer order;
    private Integer sequence;
    private boolean correct;


    public MultipleChoiceAnswerOptionDto(Option correctOption) {
        optionId = correctOption.getId();
        order = correctOption.getOrder();
        correct = correctOption.isCorrect();
    }

    public MultipleChoiceAnswerOptionDto(MultipleChoiceAnswerOption answeredOption) {
        
        optionId = answeredOption.getOption().getId();
        order = answeredOption.getAssignedOrder();
        correct = answeredOption.isCorrect();
        sequence = answeredOption.getOption().getSequence();
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getOptionId(){
        return optionId;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public boolean isCorrect() {
        return correct;
    }


    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "MultipleChoiceAnswerOptionDto{" +
                "optionId=" + optionId +
                ", order=" + order +
                ", sequence=" + sequence  +
                ", correct=" + correct  +
                '}';
    }
    
}
