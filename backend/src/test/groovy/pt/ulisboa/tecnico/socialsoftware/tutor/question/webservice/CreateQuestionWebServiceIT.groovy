package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption

class CreateQuestionWebServiceIT extends QuestionWebServiceIT {
    def "create open-ended question as a teacher"() {
        given: "a teacher"
        loginAsTeacher()
        and: "a questionDto"
        def questionDto = openEndedQuestionDto

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/courses/" + course.getId() + "/questions",
                body: mapper.writeValueAsString(questionDto),
                requestContentType: "application/json"
        )

        then: "check response status"
        response != null
        response.status == 200
        and: "correct question"
        def question = response.data
        question.id != null
        question.title == questionDto.getTitle()
        question.content == questionDto.getContent()
        question.status == Question.Status.AVAILABLE.name()
        question.questionDetailsDto.answer == ANSWER_1_CONTENT
    }

    def "create open-ended question as a student"() {
        given: "a student"
        loginAsStudent()
        and: "a questionDto"
        def questionDto = openEndedQuestionDto

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/courses/" + course.getId() + "/questions",
                body: mapper.writeValueAsString(questionDto),
                requestContentType: "application/json"
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "create multiple choice question as teacher"(){
        given: "a teacher"
        loginAsTeacher()

        and: "a multiple choice questionDto"
        def questionDto = multiChoiceQuestionDto
        
        when: "the web service is invoked"
        response = restClient.post(
                path: '/courses/'+ course.getId() +'/questions/',
                body: JsonOutput.toJson(questionDto),
                requestContentType: 'application/json'
        )

        then: "check response status "
        response != null
        response.status == 200

        and: "if it responds with the correct question"
        def newQuestionDto = response.data
        newQuestionDto.id != null
        newQuestionDto.status == questionDto.getStatus()
        newQuestionDto.title == questionDto.getTitle()
        newQuestionDto.content == questionDto.getContent()
        !newQuestionDto.questionDetailsDto.ordered
        newQuestionDto.questionDetailsDto.options.get(0).content == questionDto.questionDetailsDto.options.get(0).content
    }

    def "create multiple choice question as student"(){
        given: "a student"
        loginAsStudent()

        and: "a multiple choice questionDto"
        def questionDto = multiChoiceQuestionDto
        
        when: "the web service is invoked"
        response = restClient.post(
                path: '/courses/'+ course.getId() +'/questions/',
                body: JsonOutput.toJson(questionDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"

        def exception = thrown(HttpResponseException)
        exception.response.status == 403

    }

    def "create a Combination Item Question as a teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: "a questionDto"
        def questionDto = comboItemQuestionDto
        
        when:
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/courses/" + course.getId() + "/questions",
                body: mapper.writeValueAsString(questionDto),
                requestContentType: "application/json"
        )

        then: "check the response status"
        response != null
        response.status == 200

        and: "if it responds with the correct questiom"
        def question = response.data
        question.id != null
        question != null
        question.title == questionDto.getTitle()
        question.content == questionDto.getContent()

        def opt = question.questionDetailsDto.options.get(0)
        opt.link.get(0) == 1
        opt.content == "option"
        opt.left == true

        question.status == Question.Status.AVAILABLE.name()
    }

    def "create a Combination Item Question as a Student"(){
        given: "a student"
        loginAsStudent()
        and: "a questionDto"
        def questionDto = comboItemQuestionDto
        when:
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/courses/" + course.getId() + "/questions",
                body: mapper.writeValueAsString(questionDto),
                requestContentType: "application/json"
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
        
    }
}