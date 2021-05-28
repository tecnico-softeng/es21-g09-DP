package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion;

import java.util.List;
import java.util.stream.Collectors;

public class CombinationItemStatementQuestionDetailsDto extends StatementQuestionDetailsDto{
    private List<StatementOptionDto> options;

    public CombinationItemStatementQuestionDetailsDto(CombinationItemQuestion question) {
        this.options = question.getOptions().stream()
                .map(StatementOptionDto::new)
                .collect(Collectors.toList());
    }

    public List<StatementOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<StatementOptionDto> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "CombinationItemStatementQuestionDetailsDto{" +
                "options=" + options +
                '}';
    }
    
}
