package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswerItem;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswerItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Transient;

public class MultipleChoiceStatementAnswerDetailsDto extends StatementAnswerDetailsDto {
    private List<OptionStatementAnswerDetailsDto> answeredOptions = new ArrayList<>();

    public MultipleChoiceStatementAnswerDetailsDto() {
    }

    public MultipleChoiceStatementAnswerDetailsDto(MultipleChoiceAnswer questionAnswer) {
        if (questionAnswer.getAnsweredOptions() != null) {
            this.answeredOptions = questionAnswer.getAnsweredOptions()
                    .stream()
                    .<OptionStatementAnswerDetailsDto>map(OptionStatementAnswerDetailsDto::new)
                    .collect(Collectors.toList());
            this.answeredOptions.sort(Comparator.comparing(OptionStatementAnswerDetailsDto::getOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                                                .thenComparing(OptionStatementAnswerDetailsDto::getOptionId));
            System.out.println("\n\n\n"+answeredOptions);
            
        }
    }

    public List<OptionStatementAnswerDetailsDto> getAnsweredOptions() {
        return answeredOptions;
    }

    public void setAnsweredOptions(List<OptionStatementAnswerDetailsDto> answeredOptions) {
        this.answeredOptions = answeredOptions;
    }
    public void addAnsweredOptions(OptionStatementAnswerDetailsDto answeredOption) {
        this.answeredOptions.add(answeredOption);
    }

    @Transient
    private MultipleChoiceAnswer multipleChoiceAnswer;

    @Override
    public AnswerDetails getAnswerDetails(QuestionAnswer questionAnswer) {
        multipleChoiceAnswer = new MultipleChoiceAnswer(questionAnswer);
        questionAnswer.getQuestion().getQuestionDetails().update(this);
        return multipleChoiceAnswer;
    }

    @Override
    public boolean emptyAnswer() {
        return answeredOptions == null || answeredOptions.isEmpty();
    }

    @Override
    public QuestionAnswerItem getQuestionAnswerItem(String username, int quizId, StatementAnswerDto statementAnswerDto) {
        return new MultipleChoiceAnswerItem(username, quizId, statementAnswerDto, this);
    }

    @Override
    public void update(MultipleChoiceQuestion question) {
        multipleChoiceAnswer.setAnwseredOptions(question, this);
    }

    @Override
    public String toString() {
        return "MultipleChoiceStatementAnswerDto{" +
                "options=" + answeredOptions +
                '}';
    }
}
