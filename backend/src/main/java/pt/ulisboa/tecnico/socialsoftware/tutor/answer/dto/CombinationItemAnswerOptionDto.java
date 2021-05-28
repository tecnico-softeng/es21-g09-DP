package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import java.io.Serializable;
import java.util.*;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswerOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;

public class CombinationItemAnswerOptionDto implements Serializable {
    private Integer optionID;
    private List<Integer> links = new ArrayList<Integer>();
    private Integer sequence;

    public CombinationItemAnswerOptionDto(CombOption combOption) {
        optionID = combOption.getId();
        links = combOption.getLink();
    }

    public CombinationItemAnswerOptionDto(CombinationItemAnswerOption answeredOption) {
        optionID = answeredOption.getOptionID();
        links = answeredOption.getCombOption().getLink();
        sequence = answeredOption.getCombOption().getSequence();
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

    @Override
    public String toString() {
        return "CombinationItemAnswerOptionDto{" +
                "optionID=" + optionID +
                ", links=" + links +
                ", sequence=" + sequence +
                '}';
    }


}
