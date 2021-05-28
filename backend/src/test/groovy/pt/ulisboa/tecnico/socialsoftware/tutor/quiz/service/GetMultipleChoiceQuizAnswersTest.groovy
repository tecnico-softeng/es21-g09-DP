package pt.ulisboa.tecnico.socialsoftware.tutor.answer.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.dto.CourseExecutionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.MultipleChoiceStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OptionStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@DataJpaTest
class GetMultipleChoiceQuizAnswersTest extends SpockTest {

    def student
    def courseDto
    def quizQuestion
    def quizAnswer
    def quiz
    def question
    def option1
    def option2
    def option3

    def setup() {
        createExternalCourseAndExecution()

        courseDto = new CourseExecutionDto(externalCourseExecution)

        student = new User(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student.addCourse(externalCourseExecution)
        userRepository.save(student)

        question = new Question()
        question.setKey(1)
        question.setTitle("Question Title")
        question.setCourse(externalCourse)
        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

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
    }

    def "consult a multiple choice quiz with 1 answer"(){
        given: 'a quiz answered by the student'
        quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle("Quiz Title")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz.setCourseExecution(externalCourseExecution)

        quizQuestion = new QuizQuestion()
        quizQuestion.setSequence(1)
        quizQuestion.setQuiz(quiz)
        quizQuestion.setQuestion(question)

        def quizAnswer = new QuizAnswer()
        quizAnswer.setAnswerDate(DateHandler.now())
        quizAnswer.setCompleted(true)
        quizAnswer.setUser(student)
        quizAnswer.setQuiz(quiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer);
        answerDetails.addAnwseredOptions(option2,1)
        answerDetails.addAnwseredOptions(option3,2)
        questionAnswer.setAnswerDetails(answerDetails);

        quizRepository.save(quiz)
        quizAnswerRepository.save(quizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when:
        def quizAnswersDto = quizService.getQuizAnswers(quiz.getId())

        then: 'returns correct data'
        quizAnswersDto.getCorrectSequence().size() == 1
        quizAnswersDto.getCorrectSequence().get(0) == "B | C"
        quizAnswersDto.getQuizAnswers().size() == 1;
        quizAnswersDto.getQuizAnswers().get(0).getQuestionAnswers().size() == 1
    }
    def "consult a multiple choice quiz with no answer"(){
        given: 'a quiz answered by the student'
        quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle("Quiz Title")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setAvailableDate(LOCAL_DATE_BEFORE)
        quiz.setConclusionDate(LOCAL_DATE_TOMORROW)
        quiz.setCourseExecution(externalCourseExecution)
        quizRepository.save(quiz)

        when:
        def quizAnswersDto = quizService.getQuizAnswers(quiz.getId())

        then: 'returns correct data'
        quizAnswersDto.getCorrectSequence().size() == 0
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}