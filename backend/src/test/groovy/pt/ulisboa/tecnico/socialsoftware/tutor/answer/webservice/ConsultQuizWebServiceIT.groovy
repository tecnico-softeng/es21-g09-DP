package pt.ulisboa.tecnico.socialsoftware.tutor.answer.webservice

import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CorrectAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.CombinationItemAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.OpenEndedAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler



class ConsultQuizWebServiceIT extends AnswerWebServiceIT {
    def "consult multiple choice quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student1)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def solvedQuizDtos = response.data
        solvedQuizDtos.size() == 1
        solvedQuizDtos.get(0).statementQuiz.questions.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.get(0).sequence == 0
    }

    def "consult multiple choice quiz as student without permission"(){
        given: "a student without permission"
        loginAsStudent2()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student1)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }
  
    def "consult multiple choice quiz as teacher"(){
        given: "a student without permission"
        loginAsTeacher()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student1)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "consult open ended quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student1)
        openEndedQuizAnswer.setQuiz(openEndedQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(openEndedQuizAnswer)
        questionAnswer.setQuizQuestion(openEndedQuizQuestion)
        def answerDetails = new OpenEndedAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def solvedQuizDtos = response.data
        solvedQuizDtos.size() == 1
        solvedQuizDtos.get(0).statementQuiz.questions.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.get(0).answerDetails.answer == null
    }

    def "consult open ended quiz as student without permission"(){
        given: "a student without permission"
        loginAsStudent2()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student1)
        openEndedQuizAnswer.setQuiz(openEndedQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(openEndedQuizAnswer)
        questionAnswer.setQuizQuestion(openEndedQuizQuestion)
        def answerDetails = new OpenEndedAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "consult open ended quiz as teacher"(){
        given: "a teacher"
        loginAsTeacher()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student1)
        openEndedQuizAnswer.setQuiz(openEndedQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(openEndedQuizAnswer)
        questionAnswer.setQuizQuestion(openEndedQuizQuestion)
        def answerDetails = new OpenEndedAnswer(questionAnswer)
        questionAnswer.setAnswerDetails(answerDetails)

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "consult Combination quiz as student without permission"(){
        given: "a student without permission"
        loginAsStudent2()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student1)
        combinationItemQuizAnswer.setQuiz(combinationItemQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(combinationItemQuizAnswer)
        questionAnswer.setQuizQuestion(combinationItemQuizQuestion)
        def answerDetails = new CombinationItemAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);
        quizAnswerRepository.save(combinationItemQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }



    def "consult Combination Item quiz as student with permission"(){
        given: "a student with permission"
        loginAsStudent1()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student1)
        combinationItemQuizAnswer.setQuiz(combinationItemQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(combinationItemQuizAnswer)
        questionAnswer.setQuizQuestion(combinationItemQuizQuestion)
        def answerDetails = new CombinationItemAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(combinationItemQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def solvedQuizDtos = response.data
        solvedQuizDtos.size() == 1
        solvedQuizDtos.get(0).statementQuiz.questions.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.size() == 1
        solvedQuizDtos.get(0).statementQuiz.answers.get(0).sequence == 0
    }


    def "consult combination item quiz as teacher"(){
        given: "a teacher"
        loginAsTeacher()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student1)
        combinationItemQuizAnswer.setQuiz(combinationItemQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(combinationItemQuizAnswer)
        questionAnswer.setQuizQuestion(combinationItemQuizQuestion)
        def answerDetails = new CombinationItemAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);
        quizAnswerRepository.save(combinationItemQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/executions/"+ courseExecution.getId() +"/quizzes/solved" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }
}