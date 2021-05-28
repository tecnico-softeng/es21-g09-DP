package pt.ulisboa.tecnico.socialsoftware.tutor.answer.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CombinationItemStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OptionStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CombOptionStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@DataJpaTest
class AnswerCombinationItemQuestionTest extends SpockTest{
    def user
    def quizQuestion
    def option1
    def option2
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
        def questionDetails = new CombinationItemQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)

        option1 = new CombOption()
        option1.setContent("Option Content")
        option1.setLink(new ArrayList<Integer>())
        option1.setLeft(true)
        option1.addToLink(1)
        option1.setSequence(0)
        option1.setQuestionDetails(questionDetails)
        optionRepository.save(option1)

        option2 = new CombOption()
        option2.setContent("Option Content")
        option2.setLink(new ArrayList<Integer>())
        option2.setLeft(false)
        option2.setSequence(1)
        option2.addToLink(0)
        option2.setQuestionDetails(questionDetails)
        optionRepository.save(option2)

        quizAnswer = new QuizAnswer(user, quiz)
        quizAnswerRepository.save(quizAnswer)
    }

    def "answer a quiz with combination item questions correctly"(){
        given: 'a correct answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        def combinationItemAnswerDto = new CombinationItemStatementAnswerDetailsDto()
        def option0Links = new ArrayList<Integer>()
        option0Links.add(1)
        def option1Links = new ArrayList<Integer>()
        option1Links.add(0)
        def answeredLinks = new ArrayList<CombOptionStatementAnswerDetailsDto>()
        answeredLinks.add(new CombOptionStatementAnswerDetailsDto(option1.getId(),option0Links))
        answeredLinks.add(new CombOptionStatementAnswerDetailsDto(option2.getId(),option1Links))
        combinationItemAnswerDto.setAnsweredLinks(answeredLinks)
        statementAnswerDto.setAnswerDetails(combinationItemAnswerDto)
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

        and: 'the return value is OK'
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.getSequence() == 0
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions().size() == 2
    }

    def "answer a quiz with combination item questions incorrectly and ilegally"(){
        given: 'a correct answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        def combinationItemAnswerDto = new CombinationItemStatementAnswerDetailsDto()
        def option0Links = new ArrayList<Integer>()
        option0Links.add(0)
        def option1Links = new ArrayList<Integer>()
        option1Links.add(0)
        def answeredLinks = new ArrayList<CombOptionStatementAnswerDetailsDto>()
        answeredLinks.add(new CombOptionStatementAnswerDetailsDto(option1.getId(),option0Links))
        answeredLinks.add(new CombOptionStatementAnswerDetailsDto(option2.getId(),option1Links))
        combinationItemAnswerDto.setAnsweredLinks(answeredLinks)
        statementAnswerDto.setAnswerDetails(combinationItemAnswerDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when:
        def correctAnswers = answerService.concludeQuiz(statementQuizDto)

        then: 'the quiz is completed'
        quizAnswer.isCompleted()
        questionAnswerRepository.findAll().size() == 1

        and: 'the answer is not correct'
        def questionAnswer = questionAnswerRepository.findAll().get(0)

        and: 'the return value is OK : Ilegal Value will be treated in Frontend'
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.getSequence() == 0
        correctAnswerDto.getCorrectAnswerDetails().getCorrectOptions().size() == 2
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}


}

