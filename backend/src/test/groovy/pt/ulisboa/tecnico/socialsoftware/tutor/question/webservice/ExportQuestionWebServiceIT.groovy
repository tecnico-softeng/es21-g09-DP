package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto

class ExportQuestionWebServiceIT extends QuestionWebServiceIT {
    def questionDto
    def question

     def "export open ended question as teacher"() {
        given: "a teacher"
        loginAsTeacher()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), openEndedQuestionDto)
        question = questionRepository.findAll().get(0)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response: resp, reader: reader]
        }
        restClient.handler.success = { resp, reader ->
            [response: resp, reader: reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/' + course.getId() + '/questions/export',
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null

        cleanup:
        questionService.removeQuestion(question.getId())
    }

    def "export open ended question as student"() {
        given: "a student"
        loginAsStudent()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), openEndedQuestionDto)
        question = questionRepository.findAll().get(0)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response: resp, reader: reader]
        }
        restClient.handler.success = { resp, reader ->
            [response: resp, reader: reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/' + course.getId() + '/questions/export',
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        assert map['response'].status == 403

        cleanup:
        questionService.removeQuestion(question.getId())
    }

    def "export multiple choice question as teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), multiChoiceQuestionDto)
        question = questionRepository.findAll().get(0)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/'+ course.getId() +'/questions/export',
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null

        cleanup:
        questionService.removeQuestion(question.getId())
    }

    def "export multiple choice question as student"(){
        given: "a teacher"
        loginAsStudent()
        and: 'a multiple choice question'
        questionService.createQuestion(course.getId(), multiChoiceQuestionDto)
        question = questionRepository.findAll().get(0)
        
        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/'+ course.getId() +'/questions/export',
                requestContentType: 'application/json'
        )
        then: "exception is thrown"
        assert map['response'].status == 403

        cleanup:
        questionService.removeQuestion(question.getId())
    }
    
    def "export combination-item question as teacher"(){
        given: "a teacher"
        loginAsTeacher()
        and: 'a question'
        questionService.createQuestion(course.getId(), comboItemQuestionDto)
        question = questionRepository.findAll().get(0)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/'+ course.getId() +'/questions/export',
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null

        cleanup:
        questionRepository.deleteById(question.getId())
    }

    def "export combination-item question as student"(){
        given: "a student"
        loginAsStudent()
        and: 'a question'
        questionService.createQuestion(course.getId(), comboItemQuestionDto)
        question = questionRepository.findAll().get(0)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: '/courses/'+ course.getId() +'/questions/export',
                requestContentType: 'application/json'
        )
        then: "exception is thrown"
        assert map['response'].status == 403

        cleanup:
        questionService.removeQuestion(question.getId())
    }
}