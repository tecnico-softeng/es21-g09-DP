package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ENDED_QUESTION)
public class OpenEndedQuestion extends QuestionDetails {

    @Column(columnDefinition = "TEXT")
    private String answer;

    public OpenEndedQuestion() {
        super();
    }

    public OpenEndedQuestion(Question question, OpenEndedQuestionDto openEndedQuestionDto) {
        super(question);
        update(openEndedQuestionDto);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return new OpenEndedCorrectAnswerDto(this);
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return new OpenEndedStatementQuestionDetailsDto();
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return new OpenEndedStatementAnswerDetailsDto();
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return new OpenEndedAnswerDto();
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new OpenEndedQuestionDto(this);
    }

    public void update(OpenEndedQuestionDto question) {
        setAnswer(question.getAnswer());
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public void delete() {
        super.delete();
        answer = null;
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        return answer != null ? answer : "<no answer>";
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return answer != null ? answer : "<no answer>";
    }

    @Override
    public void accept(Visitor v) {
        v.visitQuestionDetails(this);
    }
}
