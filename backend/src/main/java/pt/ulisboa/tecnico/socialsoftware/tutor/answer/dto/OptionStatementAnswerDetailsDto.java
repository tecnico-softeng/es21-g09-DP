package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.io.Serializable;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswerOption;

public class OptionStatementAnswerDetailsDto implements Serializable{
    private Integer optionId;
    private Integer order;

    public OptionStatementAnswerDetailsDto(){
    }

    public OptionStatementAnswerDetailsDto(Integer optionId, Integer order){
        this.optionId = optionId;
        this.order = order;
    }

    public OptionStatementAnswerDetailsDto(MultipleChoiceAnswerOption option){
        this.optionId = option.getAssignedOrder();
        this.optionId = option.getOption().getId();
    }

    public Integer getOptionId(){
        return optionId;
    }

    public Integer getOrder(){
        return order;
    }

    public void setOrder(Integer order){
        this.order = order;
    }
}