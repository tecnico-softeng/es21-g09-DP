package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeFillInOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatementOptionDto implements Serializable {
    private Integer optionId;
    private String content;
    private List<Integer> link = new ArrayList<Integer>();

    public StatementOptionDto(Option option) {
        this.optionId = option.getId();
        this.content = option.getContent();
    }

    public StatementOptionDto(CodeFillInOption option) {
        this.optionId = option.getId();
        this.content = option.getContent();
    }

    public StatementOptionDto(CombOption option) {
        this.optionId = option.getId();
        this.content = option.getContent();
        this.link = option.getLink();
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getLink() {
        return link;
    }

    public void setLink(List<Integer> link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "StatementOptionDto{" +
                "optionId=" + optionId +
                ",link = " + link +
                ", content='" + content + '\'' +
                '}';
    }
}