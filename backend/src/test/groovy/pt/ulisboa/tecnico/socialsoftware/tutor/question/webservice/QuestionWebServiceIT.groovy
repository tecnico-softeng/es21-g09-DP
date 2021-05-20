package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def course
    def courseExecution
    def teacher
    def student
    def response

    def openEndedQuestionDto
    def multiChoiceQuestionDto
    def comboItemQuestionDto

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

        student = new User(USER_2_NAME, USER_2_EMAIL, USER_2_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        student.addCourse(courseExecution)
        courseExecution.addUser(student)
        userRepository.save(student)

        openEndedQuestionDto = new QuestionDto()
        openEndedQuestionDto.setQuestionDetailsDto(new OpenEndedQuestionDto())
        openEndedQuestionDto.setKey(1)
        openEndedQuestionDto.setTitle(QUESTION_1_TITLE)
        openEndedQuestionDto.setContent(QUESTION_1_CONTENT)
        openEndedQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        openEndedQuestionDto.getQuestionDetailsDto().setAnswer(ANSWER_1_CONTENT)

        multiChoiceQuestionDto = new QuestionDto()
        multiChoiceQuestionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        multiChoiceQuestionDto.setTitle(QUESTION_1_TITLE)
        multiChoiceQuestionDto.setContent(QUESTION_1_CONTENT)
        multiChoiceQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        multiChoiceQuestionDto.getQuestionDetailsDto().setOptions(options)

        comboItemQuestionDto = new QuestionDto()
        comboItemQuestionDto.setQuestionDetailsDto(new CombinationItemQuestionDto())
        comboItemQuestionDto.setKey(1)
        comboItemQuestionDto.setTitle(QUESTION_1_TITLE)
        comboItemQuestionDto.setContent(QUESTION_1_CONTENT)
        comboItemQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        def combDto1 = new CombOptionDto()
        combDto1.content = "option"
        combDto1.link = 1;
        combDto1.left = true;
        def combOptions = new ArrayList<CombOptionDto>()
        combOptions.add(combDto1)
        comboItemQuestionDto.getQuestionDetailsDto().setOptions(combOptions)
    }

    def loginAsTeacher(){
        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def loginAsStudent(){
        createdUserLogin(USER_2_EMAIL, USER_2_PASSWORD)
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        userRepository.deleteById(student.getId())
        courseExecutionRepository.deleteById(courseExecution.getId())
        courseRepository.deleteById(course.getId())
    }
}