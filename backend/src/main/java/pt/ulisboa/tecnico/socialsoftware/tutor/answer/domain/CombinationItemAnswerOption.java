package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import javax.persistence.*;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;

@Entity
public class CombinationItemAnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private CombinationItemAnswer combinationItemAnswer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CombOption combOption;

    private Integer optionID;

    public CombinationItemAnswerOption() {
    }

    public CombinationItemAnswerOption(CombOption combOption, CombinationItemAnswer combinationItemAnswer, Integer optionID) {
        setCombOption(combOption);
        setCombinationItemAnswer(combinationItemAnswer);
        setOptionID(optionID);
    }

    public void setCombinationItemAnswer(CombinationItemAnswer combinationItemAnswer) {
        this.combinationItemAnswer = combinationItemAnswer;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public CombOption getCombOption() {
        return combOption;
    }

    public void setCombOption(CombOption combOption) {
        this.combOption = combOption;
        combOption.getAnswerOptions().add(this);
    }
    public void setOptionID(Integer optionID) {
        this.optionID = optionID;
    }
    public CombinationItemAnswer getCombinationItemAnswer() {
        return combinationItemAnswer;
    }

    public Integer getOptionID() {
        return optionID;
    }

    public void remove() {
        this.combOption.getAnswerOptions().remove(this);
        this.combOption = null;
    }

    public boolean isCorrect() {
        return false;
    }

}
