package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.io.Serializable;
import java.util.*;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswerOption;

public class CombOptionStatementAnswerDetailsDto implements Serializable{
    private Integer optionID;
    private List<Integer> links;

    public CombOptionStatementAnswerDetailsDto(){
    }

    public CombOptionStatementAnswerDetailsDto(Integer _optionID, List<Integer> _links){
        this.optionID = _optionID;
        this.links = _links;
    }

    public CombOptionStatementAnswerDetailsDto(CombinationItemAnswerOption option){
        this.optionID = option.getOptionID();
        this.links = option.getCombOption().getLink();
    }

    public List<Integer> getLinks() {
        return links;
    }
    public Integer getOptionID() {
        return optionID;
    }
    public void setLinks(List<Integer> links) {
        this.links = links;
    }
    public void setOptionID(Integer optionID) {
        this.optionID = optionID;
    }
    
    public String toString(){
        return "OptionId: " + this.optionID + " Links:" + this.links;
    }
}
