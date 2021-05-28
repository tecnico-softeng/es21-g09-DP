package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION)
public class MultipleChoiceAnswer extends AnswerDetails {

    @OneToMany(mappedBy = "multipleChoiceAnswer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<MultipleChoiceAnswerOption> answeredOptions = new HashSet<>();

    public MultipleChoiceAnswer() {
        super();
    }

    public MultipleChoiceAnswer(QuestionAnswer questionAnswer){
        super(questionAnswer);
    }

    public MultipleChoiceAnswer(QuestionAnswer questionAnswer, Option option, Integer order){
        super(questionAnswer);
        answeredOptions.add(new MultipleChoiceAnswerOption(option,this,order));
    }

    public MultipleChoiceAnswer(QuestionAnswer questionAnswer, Collection<MultipleChoiceAnswerOption> options){
        super(questionAnswer);
        answeredOptions.addAll(options);
    }


    public Set<MultipleChoiceAnswerOption> getAnsweredOptions() {
        return answeredOptions;
    }

    public void addAnwseredOptions(Option option, Integer order){
        answeredOptions.add(new MultipleChoiceAnswerOption(option,this,order));
    }

    public void setAnwseredOptions(MultipleChoiceQuestion question, 
                                   MultipleChoiceStatementAnswerDetailsDto multipleChoiceStatementAnswerDetailsDto) {
        this.answeredOptions.clear();
        if (!multipleChoiceStatementAnswerDetailsDto.emptyAnswer()) {
            for(var answeredOption : multipleChoiceStatementAnswerDetailsDto.getAnsweredOptions()){
                Option option = question.getOptionById(answeredOption.getOptionId());
                this.answeredOptions.add(new MultipleChoiceAnswerOption(option, this, answeredOption.getOrder()));
            }
        }
    }

    @Override
    public boolean isCorrect() {
        int correctAmount=((MultipleChoiceQuestion)getQuestionAnswer().getQuestion().getQuestionDetails()).getCorrectAnswer().size();
        return correctAmount==answeredOptions.size() && answeredOptions.stream().allMatch(MultipleChoiceAnswerOption::isCorrect);
    }


    public void remove() {
        answeredOptions.forEach(MultipleChoiceAnswerOption::remove);
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new MultipleChoiceAnswerDto(this);
    }

    @Override
    public boolean isAnswered() {
        return !answeredOptions.isEmpty();
    }

    @Override
    public String getAnswerRepresentation() {
        if(answeredOptions.isEmpty()){
            return "-";
        }else{
            return answeredOptions.stream()
            .sorted(Comparator.comparing(MultipleChoiceAnswerOption::getAssignedOrder))
            .map(x -> MultipleChoiceQuestion.convertSequenceToLetter(x.getOption().getSequence()))
            .collect(Collectors.joining(" | "));
        }
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new MultipleChoiceStatementAnswerDetailsDto(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }
}
