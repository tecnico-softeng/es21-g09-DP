package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_OPTION;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_SEQUENCE_FOR_OPTION;

@Entity
@Table(name = "combOption")
public class CombOption implements DomainEntity {

    @Column(columnDefinition = "integer Default -1", nullable = false)
    private Integer link;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean leaft;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer sequence;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_details_id")
    private CombinationItemQuestion questionDetails;

    public CombOption() {
    }

    public CombOption(CombOptionDto combOption) {
        setSequence(combOption.getSequence());
        setContent(combOption.getContent());
        setLink(combOption.getLink());
        setLeft(combOption.isLeft());
    }

    public Integer getId() {
        return id;
    }

    /*public void setId(Integer id) {
        this.id = id;
    }*/

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        if (sequence == null || sequence < 0)
            throw new TutorException(INVALID_SEQUENCE_FOR_OPTION);

        this.sequence = sequence;
    }

    public boolean isLeft() {
        return leaft;
    }

    public void setLeft(boolean left) {
       this.leaft = left;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isBlank())
            throw new TutorException(INVALID_CONTENT_FOR_OPTION);

        this.content = content;
    }

    public Integer getLink() {
        return link;
    }

    public void setLink(Integer link) {
        this.link = link;
    }

    public CombinationItemQuestion getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(CombinationItemQuestion question) {
        this.questionDetails = question;
        question.addOption(this);
    }

    @Override
    public String toString() {
        return "CombOption{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", link=" + link +
                ", left=" + leaft +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitCombOption(this);
    }

    public void remove() {
        this.questionDetails = null;
    }
}