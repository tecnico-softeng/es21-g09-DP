package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.AnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenEndedAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenEndedStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDetailsDto;

import javax.persistence.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ENDED_QUESTION)
public class OpenEndedAnswer extends AnswerDetails {

    private String answer;

    public OpenEndedAnswer() {
        super();
    }

    public OpenEndedAnswer(QuestionAnswer questionAnswer) {
        super(questionAnswer);
    }

    public OpenEndedAnswer(QuestionAnswer questionAnswer, String answer) {
        super(questionAnswer);
        this.setAnswer(answer);
    }

    @Override
    public boolean isCorrect() {
        OpenEndedQuestion question = (OpenEndedQuestion)getQuestionAnswer().getQuizQuestion().getQuestion().getQuestionDetails();
        return this.getAnswer().equals(question.getAnswer());
    }

    public String getAnswer() { return answer; }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new OpenEndedAnswerDto(this);
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new OpenEndedStatementAnswerDetailsDto(this);
    }

    @Override
    public boolean isAnswered() {
        return this.answer != null;
    }

    @Override
    public String getAnswerRepresentation() {
        return isAnswered() ? getAnswer() : "<no answer>";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }

    @Override
    public void remove() {
        this.answer = null;
    }
}
