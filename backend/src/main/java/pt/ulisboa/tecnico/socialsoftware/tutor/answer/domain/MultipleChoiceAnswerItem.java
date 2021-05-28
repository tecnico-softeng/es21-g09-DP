package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.MultipleChoiceStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION)
public class MultipleChoiceAnswerItem extends QuestionAnswerItem {

    @ElementCollection
    private List<OptionAnswerItem> answeredOptions;

    public MultipleChoiceAnswerItem() {
    }

    public MultipleChoiceAnswerItem(String username, int quizId, StatementAnswerDto answer, MultipleChoiceStatementAnswerDetailsDto detailsDto) {
        super(username, quizId, answer);
        this.answeredOptions = detailsDto.getAnsweredOptions()
                    .stream()
                    .map(OptionAnswerItem::new)
                    .collect(Collectors.toList());
    }

    @Override
    public String getAnswerRepresentation(QuestionDetails questionDetails) {
        return questionDetails.getAnswerRepresentation(
                        answeredOptions.stream()
                        .sorted(Comparator.comparing(OptionAnswerItem::getAssignedOrder))
                        .map(OptionAnswerItem::getOptionId)
                        .collect(Collectors.toList()));
    }

    public List<OptionAnswerItem> getAnsweredOptions() {
        return answeredOptions;
    }

    public void setOptionId(List<OptionAnswerItem> options) {
        this.answeredOptions = options;
    }
}
