package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import javax.persistence.*;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;

@Entity
public class MultipleChoiceAnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MultipleChoiceAnswer multipleChoiceAnswer;

    @ManyToOne(optional = false)
    private Option option;

    private Integer assignedOrder;

    public MultipleChoiceAnswerOption() {
    }

    public MultipleChoiceAnswerOption(Option option, MultipleChoiceAnswer multipleChoiceAnswer, Integer assignedOrder) {
        setOption(option);
        setMultipleChoiceAnswer(multipleChoiceAnswer);
        setAssignedOrder(assignedOrder);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MultipleChoiceAnswer getMultipleChoiceAnswer() {
        return multipleChoiceAnswer;
    }

    public void setMultipleChoiceAnswer(MultipleChoiceAnswer multipleChoiceAnswer) {
        this.multipleChoiceAnswer = multipleChoiceAnswer;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
        option.getAnswerOptions().add(this);
    }

    public Integer getAssignedOrder() {
        return assignedOrder;
    }

    public void setAssignedOrder(Integer assignedOrder) {
        this.assignedOrder = assignedOrder;
    }

    public void remove() {
        this.option.getAnswerOptions().remove(this);
        this.option = null;
    }

    public boolean isCorrect() {
        if (assignedOrder==null){
            return option.getOrder()==null && option.isCorrect();
        }
        else{
            return assignedOrder.equals(this.option.getOrder()) && option.isCorrect();
        }
    }
    
}

