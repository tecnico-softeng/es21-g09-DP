package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;


import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationItemAnswerDto extends AnswerDetailsDto{
    private List<CombinationItemAnswerOptionDto> answeredLinks = new ArrayList<>();

    public CombinationItemAnswerDto() {
    }

    public CombinationItemAnswerDto(CombinationItemAnswer answer) {
        if (answer.getLinks() != null){
            this.answeredLinks = answer.getLinks().stream().map(CombinationItemAnswerOptionDto::new).collect(Collectors.toList());
        }
    }

    public List<CombinationItemAnswerOptionDto> getLinks() {
        return answeredLinks;
    }

    public void setLinks(List<CombinationItemAnswerOptionDto> _links) {
        this.answeredLinks = _links;
    }
}
