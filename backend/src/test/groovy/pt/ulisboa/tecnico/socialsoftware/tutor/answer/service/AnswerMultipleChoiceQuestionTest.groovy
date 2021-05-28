package pt.ulisboa.tecnico.socialsoftware.tutor.answer.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.MultipleChoiceStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OptionStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@DataJpaTest
class AnswerMultipleChoiceQuestionTest extends SpockTest {

    def user
    def quizQuestion
    def option1
    def option2
    def option3
    def quizAnswer
    def quiz

    def setup() {
        createExternalCourseAndExecution()

        user = new User(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        user.addCourse(externalCourseExecution)
        userRepository.save(user)

        quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle("Quiz Title")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

        def question = new Question()
        question.setKey(1)
        question.setTitle("Question Title")
        question.setCourse(externalCourse)
        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)

        option1 = new Option()
        option1.setContent("Option Content")
        option1.setCorrect(false)
        option1.setSequence(0)
        option1.setOrder(null)
        option1.setQuestionDetails(questionDetails)
        optionRepository.save(option1)

        option2 = new Option()
        option2.setContent("Option Content")
        option2.setCorrect(true)
        option2.setSequence(1)
        option2.setOrder(1)
        option2.setQuestionDetails(questionDetails)
        optionRepository.save(option2)

        option3 = new Option()
        option3.setContent("Option Content")
        option3.setCorrect(true)
        option3.setSequence(2)
        option3.setOrder(2)
        option3.setQuestionDetails(questionDetails)
        optionRepository.save(option3)

        quizAnswer = new QuizAnswer(user, quiz)
        quizAnswerRepository.save(quizAnswer)
    }

    def "answer a quiz with multiple choice questions correctly"(){
        given: 'a correct answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        def multipleChoiceAnswerDto = new MultipleChoiceStatementAnswerDetailsDto()
        def answeredOptions = new ArrayList<OptionStatementAnswerDetailsDto>()
        answeredOptions.add(new OptionStatementAnswerDetailsDto(option2.getId(),1))
        answeredOptions.add(new OptionStatementAnswerDetailsDto(option3.getId(),2))
        multipleChoiceAnswerDto.setAnsweredOptions(answeredOptions)
        statementAnswerDto.setAnswerDetails(multipleChoiceAnswerDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when:
        def correctAnswers = answerService.concludeQuiz(statementQuizDto)

        then: 'the quiz is completed'
        quizAnswer.isCompleted()
        questionAnswerRepository.findAll().size() == 1

        and: 'the answer is correct'
        def questionAnswer = questionAnswerRepository.findAll().get(0)
        questionAnswer.getAnswerDetails().isCorrect()

        and: 'the return value is OK'
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.getSequence() == 0
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions().size() == 3
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions()
                        .stream().filter(x->x.correct).count() == 2
    }

    def "answer a quiz with ordered multiple choice question incorrectly"(){
        given: 'a correct answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        def multipleChoiceAnswerDto = new MultipleChoiceStatementAnswerDetailsDto()
        def answeredOptions = new ArrayList<OptionStatementAnswerDetailsDto>()
        answeredOptions.add(new OptionStatementAnswerDetailsDto(option2.getId(),null))
        multipleChoiceAnswerDto.setAnsweredOptions(answeredOptions)
        statementAnswerDto.setAnswerDetails(multipleChoiceAnswerDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when:
        def correctAnswers = answerService.concludeQuiz(statementQuizDto)

        then: 'the quiz is completed'
        quizAnswer.isCompleted()
        questionAnswerRepository.findAll().size() == 1

        and: 'the answer is correct'
        def questionAnswer = questionAnswerRepository.findAll().get(0)
        !questionAnswer.getAnswerDetails().isCorrect()

        and: 'the return value is OK'
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.getSequence() == 0
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions().size() == 3
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions()
                .stream().filter(x->x.correct).count() == 2

    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}