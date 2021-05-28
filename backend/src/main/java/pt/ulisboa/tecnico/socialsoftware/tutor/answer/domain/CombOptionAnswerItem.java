package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import java.util.*;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CombOptionStatementAnswerDetailsDto;

@Embeddable
public class CombOptionAnswerItem {
    private Integer optionId;
    //private Integer link;
    CombOptionStatementAnswerDetailsDto combOptionStatementAnswerDto;
    
    public CombOptionAnswerItem(){
    }
    
    public CombOptionAnswerItem(CombOptionStatementAnswerDetailsDto _combOptionStatementAnswerDto) {
        optionId = _combOptionStatementAnswerDto.getOptionID();
        combOptionStatementAnswerDto = _combOptionStatementAnswerDto;
    }
    
    public Integer getOptionId() {
        return this.optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public List<Integer> getLinks() {
        return this.combOptionStatementAnswerDto.getLinks();
    }

    /*public void setLinks(ArrayList<Integer> _links) {
        this.links = _links;
    }*/
}

