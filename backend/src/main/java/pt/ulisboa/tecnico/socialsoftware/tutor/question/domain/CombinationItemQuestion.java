package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuestionDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.COMBINATION_ITEM_QUESTION)
public class CombinationItemQuestion extends QuestionDetails{
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CombOption> combOptions = new ArrayList<>();

    public CombinationItemQuestion() { super();}

    public CombinationItemQuestion(Question question, CombinationItemQuestionDto questionDto) {
        super(question);
        setOptions(questionDto.getOptions());
    }
    
    public List<CombOption> getOptions() { return combOptions; }

    public void setOptions(List<CombOptionDto> optionDtos) {
        for (CombOption option: this.combOptions) {
            option.remove();
        }
        this.combOptions.clear();

        int index = 0;
        for (CombOptionDto optionDto : optionDtos) {
            optionDto.setSequence(index++);
            new CombOption(optionDto).setQuestionDetails(this);
        }
    }

    public void addOption(CombOption option) {
        combOptions.add(option);
    }


    public void update(CombinationItemQuestionDto questionDetails) {
        setOptions(questionDetails.getOptions());
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }

    public void visitOptions(Visitor visitor) {
        for (CombOption option : this.getOptions()) {
            option.accept(visitor);
        }
    }

    public CombOption getOptionById(Integer optionId) {
        return combOptions.stream()
                .filter(combOption -> combOption.getId().equals(optionId))
                .findAny()
                .orElse(null); // FIXME: throw exception
    }

    /*public List<CombOption> getCorrectAnswer() {
        return combOptions;
    }*/

    @Override
    public  CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return new CombinationItemCorrectAnswerDto(this);
    }

    @Override
    public  StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return new CombinationItemStatementQuestionDetailsDto(this);
    }

    @Override
    public  StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return new CombinationItemStatementAnswerDetailsDto();
    }

    @Override
    public  AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return new CombinationItemAnswerDto();
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new CombinationItemQuestionDto(this);
    }

    @Override
    public void delete() { 
        super.delete();
        for (CombOption option : this.combOptions) {
            option.remove();
        }
        this.combOptions.clear(); 
    }

    @Override
    public String toString() {
        return "CombinationItem{ Options" + combOptions + '}';
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) { return ""; }

    @Override
    public String getCorrectAnswerRepresentation(){
        return "";
    }
}