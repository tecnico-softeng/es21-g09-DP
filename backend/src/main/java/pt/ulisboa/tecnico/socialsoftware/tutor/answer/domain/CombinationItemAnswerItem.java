package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CombinationItemStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(Question.QuestionTypes.COMBINATION_ITEM_QUESTION)
public class CombinationItemAnswerItem extends QuestionAnswerItem {
    @ElementCollection
    private List<CombOptionAnswerItem> answeredOptions;

    public CombinationItemAnswerItem() {
    }

    public CombinationItemAnswerItem(String username, int quizId, StatementAnswerDto answer, CombinationItemStatementAnswerDetailsDto detailsDto) {
        super(username, quizId, answer);
        this.answeredOptions = detailsDto.getAnsweredLinks()
                    .stream()
                    .map(CombOptionAnswerItem::new)
                    .collect(Collectors.toList());
    }

    @Override
    public String getAnswerRepresentation(QuestionDetails questionDetails) {
        return "";
    }

    public List<CombOptionAnswerItem> getAnsweredOptions() {
        return answeredOptions;
    }

    public void setAnsweredOptions(List<CombOptionAnswerItem> options) {
        this.answeredOptions = options;
    }
}

