package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswerItem;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Transient;

public class CombinationItemStatementAnswerDetailsDto extends StatementAnswerDetailsDto {
    private List<CombOptionStatementAnswerDetailsDto> answeredLinks = new ArrayList<>();

    public CombinationItemStatementAnswerDetailsDto() {
    }

    public CombinationItemStatementAnswerDetailsDto(CombinationItemAnswer questionAnswer) {
        if (questionAnswer.getLinks() != null) {
            this.answeredLinks = questionAnswer.getLinks()
            .stream()
            .<CombOptionStatementAnswerDetailsDto>map(CombOptionStatementAnswerDetailsDto::new)
            .collect(Collectors.toList());
        }
    }

    public List<CombOptionStatementAnswerDetailsDto> getAnsweredLinks() {
        return answeredLinks;
    }

    public void setAnsweredLinks(List<CombOptionStatementAnswerDetailsDto> _answeredLinks) {
        this.answeredLinks = _answeredLinks;
    }
 

    @Transient
    private CombinationItemAnswer combinationItemAnswer;

    @Override
    public AnswerDetails getAnswerDetails(QuestionAnswer questionAnswer) {
        combinationItemAnswer = new CombinationItemAnswer(questionAnswer);
        questionAnswer.getQuestion().getQuestionDetails().update(this);
        return combinationItemAnswer;
    }

    @Override
    public boolean emptyAnswer() {
        return answeredLinks == null || answeredLinks.isEmpty();
    }

    @Override
    public QuestionAnswerItem getQuestionAnswerItem(String username, int quizId, StatementAnswerDto statementAnswerDto) {
        return new CombinationItemAnswerItem(username, quizId, statementAnswerDto, this);
    }

    @Override
    public void update(CombinationItemQuestion question) {
        combinationItemAnswer.setAnwseredLinks(question, this);
    }

    @Override
    public String toString() {
        return "CombinationItemStatementAnswerDto{" +
                "Answered Links=" + answeredLinks +
                '}';
    }
}
