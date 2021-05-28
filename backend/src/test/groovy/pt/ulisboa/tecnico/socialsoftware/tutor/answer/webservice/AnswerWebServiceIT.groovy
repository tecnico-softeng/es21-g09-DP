package pt.ulisboa.tecnico.socialsoftware.tutor.answer.webservice

import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnswerWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def courseExecution
    def teacher
    def student1
    def student2
    def response
    def multipleChoiceQuiz
    def openEndedQuiz
    def multipleChoiceQuizQuestion
    def openEndedQuizQuestion
    def combinationItemQuiz
    def combinationItemQuizAnswer
    def combinationItemQuizQuestion
    def combOption1
    def combOption2


    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)

        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL, User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(courseExecution)
        courseExecution.addUser(teacher)
        userRepository.save(teacher)

        student1 = new User(USER_2_NAME, USER_2_EMAIL, USER_2_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student1.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        student1.addCourse(courseExecution)
        courseExecution.addUser(student1)
        userRepository.save(student1)

        student2 = new User(USER_3_NAME, USER_3_EMAIL, USER_3_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student2.authUser.setPassword(passwordEncoder.encode(USER_3_PASSWORD))
        userRepository.save(student2)

        createMultipleChoiceQuiz()
        createCombinationItemQuiz()
        createOpenEndedQuiz()

    }

    def loginAsTeacher(){
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def loginAsStudent1(){
        createdUserLogin(USER_2_EMAIL, USER_2_PASSWORD)
    }

    def loginAsStudent2(){
        createdUserLogin(USER_3_EMAIL, USER_3_PASSWORD)
    }

    def createMultipleChoiceQuiz(){
        multipleChoiceQuiz = new Quiz()
        multipleChoiceQuiz.setKey(1)
        multipleChoiceQuiz.setTitle("Multiple Choice Quiz")
        multipleChoiceQuiz.setType(Quiz.QuizType.PROPOSED.toString())
        multipleChoiceQuiz.setCourseExecution(courseExecution)
        multipleChoiceQuiz.setAvailableDate(DateHandler.now())
        quizRepository.save(multipleChoiceQuiz)

        def question = new Question()
        question.setKey(1)
        question.setTitle("Multiple Choice Question")
        question.setCourse(course)
        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        multipleChoiceQuizQuestion = new QuizQuestion(multipleChoiceQuiz, question, 0)
        quizQuestionRepository.save(multipleChoiceQuizQuestion)

        def option1 = new Option()
        option1.setContent("Option Content")
        option1.setCorrect(false)
        option1.setSequence(0)
        option1.setOrder(null)
        option1.setQuestionDetails(questionDetails)
        optionRepository.save(option1)

        def option2 = new Option()
        option2.setContent("Option Content")
        option2.setCorrect(true)
        option2.setSequence(1)
        option2.setOrder(null)
        option2.setQuestionDetails(questionDetails)
        optionRepository.save(option2)

    }

    def createOpenEndedQuiz() {
        openEndedQuiz = new Quiz()
        openEndedQuiz.setKey(2)
        openEndedQuiz.setTitle("Open Ended Quiz")
        openEndedQuiz.setType(Quiz.QuizType.PROPOSED.toString())
        openEndedQuiz.setCourseExecution(courseExecution)
        openEndedQuiz.setAvailableDate(DateHandler.now())
        quizRepository.save(openEndedQuiz)

        def question = new Question()
        question.setKey(2)
        question.setTitle("Open Ended Question")
        question.setCourse(course)
        def questionDetails = new OpenEndedQuestion()
        questionDetails.setAnswer(ANSWER_1_CONTENT)
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        openEndedQuizQuestion = new QuizQuestion(openEndedQuiz, question, 0)
        quizQuestionRepository.save(openEndedQuizQuestion)
    }

    def createCombinationItemQuiz(){
        combinationItemQuiz = new Quiz()
        combinationItemQuiz.setKey(1)
        combinationItemQuiz.setTitle("Combination Item Quiz")
        combinationItemQuiz.setType(Quiz.QuizType.PROPOSED.toString())
        combinationItemQuiz.setCourseExecution(courseExecution)
        combinationItemQuiz.setAvailableDate(DateHandler.now())
        quizRepository.save(combinationItemQuiz)

        def question = new Question()
        question.setKey(1)
        question.setTitle("Combination Item Question")
        question.setCourse(course)
        def questionDetails = new CombinationItemQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        combinationItemQuizQuestion = new QuizQuestion(combinationItemQuiz, question, 0)
        quizQuestionRepository.save(combinationItemQuizQuestion)

        combOption1 = new CombOption()
        combOption1.setContent("Option Content")
        combOption1.setLink(new ArrayList<Integer>())
        combOption1.link.add(1)
        combOption1.leaft = true
        combOption1.setSequence(0)
        combOption1.setQuestionDetails(questionDetails)
        optionRepository.save(combOption1)

        combOption2 = new CombOption()
        combOption2.setContent("Option Content")
        combOption2.setLink(new ArrayList<Integer>())
        combOption2.link.add(0)
        combOption2.leaft = false
        combOption2.setSequence(1)
        combOption2.setQuestionDetails(questionDetails)
        optionRepository.save(combOption2)

    }


    def cleanup() {
        userRepository.deleteById(teacher.getId())
        userRepository.deleteById(student1.getId())
        userRepository.deleteById(student2.getId())
        courseExecutionRepository.deleteById(courseExecution.getId())
        courseRepository.deleteById(course.getId())
    }
}