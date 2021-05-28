package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Entity
@DiscriminatorValue(Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION)
public class MultipleChoiceQuestion extends QuestionDetails {

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean ordered;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Option> options = new ArrayList<>();

    public MultipleChoiceQuestion() {
        super();
    }

    public MultipleChoiceQuestion(Question question, MultipleChoiceQuestionDto questionDto) {
        super(question);
        setOrdered(questionDto.isOrdered());
        setOptions(questionDto.getOptions());
    }

    public boolean isOrdered(){
        return ordered;
    }

    public void setOrdered(boolean ordered){
        this.ordered = ordered;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDto> optionDtos) {
        if (optionDtos.stream().filter(OptionDto::isCorrect).count() < 1) {
            throw new TutorException(ONE_CORRECT_OPTION_NEEDED);
        }
        if (optionDtos.stream().anyMatch(o -> !o.isCorrect() && o.getOrder()!=null)){
            throw new TutorException(WRONG_OPTION_WITH_ORDER);
        }
        if (!ordered && optionDtos.stream().anyMatch(o -> o.getOrder()!=null)){
            throw new TutorException(NEED_ORDERED_FLAG);
        }
        if (ordered && optionDtos.stream().anyMatch(o -> o.isCorrect() && o.getOrder()==null)){
            throw new TutorException(CORRECT_OPTION_WITHOUT_ORDER);
        }
        if (ordered && optionDtos.stream().filter(x -> x.isCorrect()).count() < 2) {
            throw new TutorException(TWO_CORRECT_ORDERED_OPTION_NEEDED);
        }

        List<Integer> list =  optionDtos.stream().map(x->x.getOrder()).filter(x -> x!=null).sorted().collect(Collectors.toList());
        if (ordered && !java.util.stream.IntStream.range(0, list.size()).allMatch(value -> value + 1 == list.get(value))){
            throw new TutorException(INVALID_OPTIONS_ORDER);
        }

        for (Option option: this.options) {
            option.remove();
        }
        this.options.clear();

        int index = 0;
        for (OptionDto optionDto : optionDtos) {
            optionDto.setSequence(index++);
            new Option(optionDto).setQuestionDetails(this);
        }
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public Option getOptionById(Integer optionId) {
        return options.stream()
                .filter(option -> option.getId().equals(optionId))
                .findAny()
                .orElseThrow(() -> new TutorException(QUESTION_OPTION_MISMATCH, this.getQuestion().getId(), optionId));
    }

    public void update(MultipleChoiceQuestionDto questionDetails) {
        setOrdered(questionDetails.isOrdered());
        setOptions(questionDetails.getOptions());
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        return getCorrectAnswer().stream()
                .map(sequence -> convertSequenceToLetter(sequence))
                .collect(Collectors.joining(" | "));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }

    public void visitOptions(Visitor visitor) {
        for (Option option : this.getOptions()) {
            option.accept(visitor);
        }
    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return new MultipleChoiceCorrectAnswerDto(this);
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return new MultipleChoiceStatementQuestionDetailsDto(this);
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return new MultipleChoiceStatementAnswerDetailsDto();
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return new MultipleChoiceAnswerDto();
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new MultipleChoiceQuestionDto(this);
    }

    public List<Integer> getCorrectAnswer() {
        List<Option> correctOptions = this.getOptions().stream()
                .filter(Option::isCorrect)
                .collect(Collectors.toList());
        if (correctOptions.isEmpty()){
            new TutorException(NO_CORRECT_OPTION);
        }
        if (ordered){
            return correctOptions.stream()
                        .sorted(Comparator.comparing(Option::getOrder))
                        .map(Option::getSequence)
                        .collect(Collectors.toList());
        }
        else {
            return correctOptions.stream()
                        .map(Option::getSequence)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public void delete() {
        super.delete();
        for (Option option : this.options) {
            option.remove();
        }
        this.options.clear();
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "options=" + options +
                '}';
    }

    public static String convertSequenceToLetter(Integer correctAnswer) {
        return correctAnswer != null ? Character.toString('A' + correctAnswer) : "-";
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        var result = this.options
                .stream()
                .filter(x -> selectedIds.contains(x.getId()))
                .map(x -> convertSequenceToLetter(x.getSequence()))
                .collect(Collectors.joining("|"));
        return !result.isEmpty() ? result : "-";
    }
}