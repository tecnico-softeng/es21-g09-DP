package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswerOption;
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
import java.util.List;
import java.util.ArrayList;


import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_OPTION;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_SEQUENCE_FOR_OPTION;

@Entity
@Table(name = "combOption")
public class CombOption implements DomainEntity {
    /*As ligacoes vao ser definidas pelos ID's das opcoes que estao ligadas*/
    @ElementCollection(fetch=FetchType.EAGER)
    private List<Integer> link = new ArrayList<Integer>();

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "combOption", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<CombinationItemAnswerOption> answerLinks = new HashSet<>();

    public CombOption() {
    }

    public CombOption(CombOptionDto combOption) {
        setSequence(combOption.getSequence());
        setContent(combOption.getContent());
        setLink(combOption.getLink());
        setLeft(combOption.isLeft());
    }

    public Set<CombinationItemAnswerOption> getAnswerOptions() {
        return answerLinks;
    }

    public void addAnswerOptions(CombinationItemAnswerOption questionAnswer) {
        answerLinks.add(questionAnswer);
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

    public List<Integer> getLink() {
        return link;
    }

    public void setLink(List<Integer> link) {
        this.link = link;
    }

    public void addToLink(Integer _link){
        link.add(_link);
    }

    public void removeFromLink(Integer _link){
        link.remove(_link);
    }

    public boolean isInLink(Integer _link){
        return link.contains(_link);
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