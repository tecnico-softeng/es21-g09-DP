package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion;

public class OpenEndedCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private String correctAnswer;

    public OpenEndedCorrectAnswerDto(OpenEndedQuestion question) {
        this.correctAnswer = question.getAnswer();
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String answer) {
        this.correctAnswer = answer;
    }

    @Override
    public String toString() {
        return "OpenEndedCorrectAnswerDto{" +
                "correctAnswer=" + correctAnswer +
                '}';
    }
}