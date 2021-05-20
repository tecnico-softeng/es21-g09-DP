package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import java.io.Serializable;

public class CombOptionDto implements Serializable {
    private Integer id;
    private Integer sequence;
    private Integer link;
    private String content;
    private boolean left;

    public CombOptionDto() {
    }

    public CombOptionDto(CombOption combOption) {
        this.id = combOption.getId();
        this.sequence = combOption.getSequence();
        this.content = combOption.getContent();
        this.link = combOption.getLink();
        this.left = combOption.isLeft();
    }

    public CombOptionDto(Option option) {
        this.id = option.getId();
        this.sequence = option.getSequence();
        this.content = option.getContent();
        this.link = -1;
        this.left = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getLink() {
        return link;
    }

    public void setLink(Integer link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "OptionDto{" +
                "id=" + id +
                ", link=" + link +
                ", content='" + content + '\'' +
                ", isLeft='" + left + '\'' +
                '}';
    }
}