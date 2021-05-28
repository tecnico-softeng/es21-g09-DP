package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.OpenEndedAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.OpenEndedAnswerItem;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswerItem;

import javax.persistence.Transient;

public class OpenEndedStatementAnswerDetailsDto extends StatementAnswerDetailsDto {

    private String answer;

    @Transient
    private OpenEndedAnswer createdOpenEndedAnswer;

    public OpenEndedStatementAnswerDetailsDto() {
    }

    public OpenEndedStatementAnswerDetailsDto(OpenEndedAnswer questionAnswer) {
        this.answer = questionAnswer.getAnswer();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public AnswerDetails getAnswerDetails(QuestionAnswer questionAnswer) {
        createdOpenEndedAnswer = new OpenEndedAnswer(questionAnswer);
        questionAnswer.getQuestion().getQuestionDetails().update(this);
        return createdOpenEndedAnswer;
    }

    @Override
    public boolean emptyAnswer() {
        return answer == null;
    }

    @Override
    public QuestionAnswerItem getQuestionAnswerItem(String username, int quizId, StatementAnswerDto statementAnswerDto) {
        return new OpenEndedAnswerItem(username, quizId, statementAnswerDto, this);
    }

    @Override
    public void update(OpenEndedQuestion question) {
        createdOpenEndedAnswer.setAnswer(answer);
    }

    @Override
    public String toString() {
        return "OpenEndedStatementAnswerDto{" +
                "answer=" + answer +
                '}';
    }
}
