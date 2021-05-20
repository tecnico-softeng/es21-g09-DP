package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovyx.net.http.HttpResponseException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto

class RemoveQuestionWebServiceIT extends QuestionWebServiceIT {
    def question

    def "remove open-ended question as teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: 'open-ended question'
        questionService.createQuestion(course.getId(), openEndedQuestionDto)
        question = questionRepository.findAll().get(0)

        when: "the web service is invoked"
        response = restClient.delete(
                path: "/questions/" + question.getId(),
                requestContentType: "application/json"
        )

        then: "check response status"
        response != null
        response.status == 200
        and: "the question has been removed from the database"
        questionRepository.findById(question.getId()).isEmpty()
    }

    def "remove open-ended question as student"(){
        given: "a student"
        loginAsStudent()
        and: 'open-ended question'
        questionService.createQuestion(course.getId(), openEndedQuestionDto)
        question = questionRepository.findAll().get(0)

        when: "the web service is invoked"
        response = restClient.delete(
                path: "/questions/" + question.getId(),
                requestContentType: "application/json"
                )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "remove multiple choice question as teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), multiChoiceQuestionDto)
        question = questionRepository.findAll().get(0)

        when:
        response = restClient.delete(
                path: '/questions/'+question.getId(),
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200
        and: "the question has been removed from the database"
        questionRepository.findById(question.getId()).isEmpty()
    }

    def "remove multiple choice question as student"(){
        given: "a student"
        loginAsStudent()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), multiChoiceQuestionDto)
        question = questionRepository.findAll().get(0)

        when:
        response = restClient.delete(
                path: '/questions/'+question.getId(),
                requestContentType: 'application/json'
                )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "remove CombinationItemQuestion as teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: 'a CombinationItemQuestion'
        questionService.createQuestion(course.getId(), comboItemQuestionDto)
        question = questionRepository.findAll().get(0)

        when: "the web service is invoked"
        response = restClient.delete(
                path: "/questions/" + question.getId(),
                requestContentType: "application/json"
        )

        then: "check response status"
        response != null
        response.status == 200
        and: "the question has been removed from the database"
        questionRepository.findById(question.getId()).isEmpty()
    }

    def "remove CombinationItemQuestion as student"(){
        given: "a student"
        loginAsStudent()
        
        and: 'a CombinationItemQuestion'
        questionService.createQuestion(course.getId(), comboItemQuestionDto)
        question = questionRepository.findAll().get(0)

        when: "the web service is invoked"
        response = restClient.delete(
                path: "/questions/" + question.getId(),
                requestContentType: "application/json"
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }
}