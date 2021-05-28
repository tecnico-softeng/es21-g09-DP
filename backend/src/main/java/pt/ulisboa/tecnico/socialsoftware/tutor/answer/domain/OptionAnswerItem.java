package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import javax.persistence.Embeddable;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OptionStatementAnswerDetailsDto;

@Embeddable
public class OptionAnswerItem {
    private Integer optionId;
    private Integer assignedOrder;

    
    public OptionAnswerItem(){
    }
    
    public OptionAnswerItem(OptionStatementAnswerDetailsDto optionStatementAnswerDto) {
        optionId = optionStatementAnswerDto.getOptionId();
        assignedOrder = optionStatementAnswerDto.getOrder();
    }
    
    public Integer getOptionId() {
        return this.optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getAssignedOrder() {
        return this.assignedOrder;
    }

    public void setAssignedOrder(Integer assignedOrder) {
        this.assignedOrder = assignedOrder;
    }
}
