package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CombOptionDto implements Serializable {
    private Integer id;
    private Integer sequence;
    private List<Integer> link  = new ArrayList<Integer>();
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
        this.link = new ArrayList<Integer>();
        this.sequence = option.getSequence();
        this.content = option.getContent();
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

    public List<Integer> getLink() {
        return link;
    }

    public void setLink(List<Integer> link) {
        this.link = link;
    }

    public void addToLink(Integer _link){
        this.link.add(_link);
    }

    public void isInLink(Integer _link){
        this.link.contains(_link);
    }

    public void removeFromLink(Integer _link){
        this.link.remove(_link);
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