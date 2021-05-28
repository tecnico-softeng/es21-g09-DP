package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;
import javax.persistence.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.COMBINATION_ITEM_QUESTION)
public class CombinationItemAnswer extends AnswerDetails {
    @OneToMany(mappedBy = "combinationItemAnswer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<CombinationItemAnswerOption> answeredLinks = new HashSet<>();

    Integer correctAmount;

    public CombinationItemAnswer() {
        super();
    }

    public CombinationItemAnswer(QuestionAnswer questionAnswer, Collection<CombinationItemAnswerOption> links){
        super(questionAnswer);
        answeredLinks.addAll(links);
    }

    public CombinationItemAnswer(QuestionAnswer questionAnswer){
        super(questionAnswer);
    }

    public void setAnwseredLinks(CombinationItemQuestion question, CombinationItemStatementAnswerDetailsDto combinationItemStatementAnswerDetailsDto) {
        this.answeredLinks.clear();
        if (!combinationItemStatementAnswerDetailsDto.emptyAnswer()) {
            for(var answeredOption : combinationItemStatementAnswerDetailsDto.getAnsweredLinks()){
                CombOption combOption = question.getOptionById(answeredOption.getOptionID());
                this.answeredLinks.add(new CombinationItemAnswerOption(combOption, this, answeredOption.getOptionID()));
            }
        }
        //correctAmount = question.getCorrectAnswer().size();
    }

    public Set<CombinationItemAnswerOption> getLinks(){
        return answeredLinks;
    }

    public void addAnwseredOptions(CombOption option){
        answeredLinks.add(new CombinationItemAnswerOption(option,this,option.getId()));
    }

    /*public void setLinks(Set<CombinationItemAnswerOption> _links){
        this.answeredLinks = _links;
    }*/

    @Override
    public boolean isCorrect() {
        return this.answeredLinks != null && answeredLinks.stream().allMatch(CombinationItemAnswerOption::isCorrect);
    }

    @Override
    public void remove() {
        
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new CombinationItemAnswerDto(this);
    }

    @Override
    public boolean isAnswered() {
        return this.answeredLinks != null;
    }

    @Override
    public String getAnswerRepresentation() {
        //return this.getOption() != null ? MultipleChoiceQuestion.convertSequenceToLetter(this.getOption().getSequence()) : "-";
        return "";
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new CombinationItemStatementAnswerDetailsDto(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }

}