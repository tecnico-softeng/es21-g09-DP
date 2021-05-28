package pt.ulisboa.tecnico.socialsoftware.tutor.answer.webservice

import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CorrectAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler



class AnswerQuizWebServiceIT extends AnswerWebServiceIT {

    def "answer multiple choice quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer'
        def multipleChoiceQuizAnswer = new QuizAnswer(student1, multipleChoiceQuiz)
        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = multipleChoiceQuiz.getId()
        statementQuizDto.quizAnswerId = multipleChoiceQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(multipleChoiceQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def correctAnswers = response.data
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.sequence == 0
        correctAnswerDto.correctAnswerDetails.correctOptions.size() == 2
    }

    def "answer multiple choice quiz as teacher"(){
        given: "a student with permission"
        loginAsTeacher()

        and: 'an empty answer'
        def multipleChoiceQuizAnswer = new QuizAnswer(student1, multipleChoiceQuiz)
        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = multipleChoiceQuiz.getId()
        statementQuizDto.quizAnswerId = multipleChoiceQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(multipleChoiceQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "answer multiple choice quiz as student without permission"(){
        given: "a student with permission"
        loginAsStudent2()

        and: 'an empty answer'
        def multipleChoiceQuizAnswer = new QuizAnswer(student1, multipleChoiceQuiz)
        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = multipleChoiceQuiz.getId()
        statementQuizDto.quizAnswerId = multipleChoiceQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(multipleChoiceQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "answer open ended quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer'
        def openEndedQuizAnswer = new QuizAnswer(student1, openEndedQuiz)
        quizAnswerRepository.save(openEndedQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = openEndedQuiz.getId()
        statementQuizDto.quizAnswerId = openEndedQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(openEndedQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def correctAnswers = response.data
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.sequence == 0
        correctAnswerDto.correctAnswerDetails.correctAnswer == ANSWER_1_CONTENT
    }

    def "answer open ended quiz as teacher"() {
        given: "a student with permission"
        loginAsTeacher()

        and: 'an empty answer'
        def openEndedQuizAnswer = new QuizAnswer(student1, openEndedQuiz)
        quizAnswerRepository.save(openEndedQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = openEndedQuiz.getId()
        statementQuizDto.quizAnswerId = openEndedQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(openEndedQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "answer open ended quiz as student without permission"(){
        given: "a student without permission"
        loginAsStudent2()

        and: 'an empty answer'
        def openEndedQuizAnswer = new QuizAnswer(student1, openEndedQuiz)
        quizAnswerRepository.save(openEndedQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = openEndedQuiz.getId()
        statementQuizDto.quizAnswerId = openEndedQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(openEndedQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "answer combination item quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer'
        combinationItemQuizAnswer = new QuizAnswer(student1, combinationItemQuiz)
        quizAnswerRepository.save(combinationItemQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = combinationItemQuiz.getId()
        statementQuizDto.quizAnswerId = combinationItemQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(combinationItemQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def correctAnswers = response.data
        correctAnswers.size() == 1
        def correctAnswerDto = correctAnswers.get(0)
        correctAnswerDto.sequence == 0
        correctAnswerDto.correctAnswerDetails.correctOptions.size() == 2
    }

    def "answer combination item quiz as teacher"() {
        given: "a student with permission"
        loginAsTeacher()

        and: 'an empty answer'
        combinationItemQuizAnswer = new QuizAnswer(student1, combinationItemQuiz)
        quizAnswerRepository.save(combinationItemQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = combinationItemQuiz.getId()
        statementQuizDto.quizAnswerId = combinationItemQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(combinationItemQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "answer combination item quiz as student without permission"(){
        given: "a student without permission"
        loginAsStudent2()

        and: 'an empty answer'
        combinationItemQuizAnswer = new QuizAnswer(student1, combinationItemQuiz)
        quizAnswerRepository.save(combinationItemQuizAnswer)
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = combinationItemQuiz.getId()
        statementQuizDto.quizAnswerId = combinationItemQuizAnswer.getId()
        def statementAnswerDto = new StatementAnswerDto()
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(combinationItemQuizAnswer.getQuestionAnswers().get(0).getId())
        statementQuizDto.getAnswers().add(statementAnswerDto)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.post(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/conclude" ,
                body: mapper.writeValueAsString(statementQuizDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

}