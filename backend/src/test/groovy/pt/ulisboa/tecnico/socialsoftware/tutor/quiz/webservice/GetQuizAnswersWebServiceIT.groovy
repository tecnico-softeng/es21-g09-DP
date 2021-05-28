package pt.ulisboa.tecnico.socialsoftware.tutor.quiz.webservice

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



class GetQuizAnswersWebServiceIT extends QuizWebServiceIT {
    def "get answers of a multiple choice quiz as teacher with permission"(){
        given: "a teacher with permission"
        loginAsTeacher1()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def quizAnswersDto = response.data
        quizAnswersDto.correctSequence.size() == 1
        quizAnswersDto.correctSequence.get(0) == "B"
        quizAnswersDto.quizAnswers.size() == 1;
        quizAnswersDto.quizAnswers.get(0).questionAnswers.size() == 1
    }

    def "get answers of a multiple choice quiz as teacher without permission"(){
        given: "a teacher with permission"
        loginAsTeacher2()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "get answers of a multiple choice quiz as student"(){
        given: "a teacher with permission"
        loginAsStudent()

        and: 'an empty answer to the quiz'
        def multipleChoiceQuizAnswer = new QuizAnswer()
        multipleChoiceQuizAnswer.setAnswerDate(DateHandler.now())
        multipleChoiceQuizAnswer.setCompleted(true)
        multipleChoiceQuizAnswer.setUser(student)
        multipleChoiceQuizAnswer.setQuiz(multipleChoiceQuiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setSequence(0)
        questionAnswer.setQuizAnswer(multipleChoiceQuizAnswer)
        questionAnswer.setQuizQuestion(multipleChoiceQuizQuestion)
        def answerDetails = new MultipleChoiceAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(multipleChoiceQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        when: "the web service is invoked"
        def mapper = new ObjectMapper()
        response = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "get answers of a combination Item quiz as teacher with permission"(){
        given: "a teacher with permission"
        loginAsTeacher1()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def quizAnswersDto = response.data
        quizAnswersDto.correctSequence.size() == 1
        quizAnswersDto.quizAnswers.size() == 1;
        quizAnswersDto.quizAnswers.get(0).questionAnswers.size() == 1
    }
    
    

    def "get answers of a combination item quiz as teacher without permission"(){
        given: "a teacher with permission"
        loginAsTeacher2()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "get answers of a combination item quiz as student"(){
        given: "a teacher with permission"
        loginAsStudent()

        and: 'an empty answer to the quiz'
        def combinationItemQuizAnswer = new QuizAnswer()
        combinationItemQuizAnswer.setAnswerDate(DateHandler.now())
        combinationItemQuizAnswer.setCompleted(true)
        combinationItemQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "get answers of a open ended quiz as teacher with permission"(){
        given: "a teacher with permission"
        loginAsTeacher1()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ openEndedQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "check response status"
        response != null
        response.status == 200

        and: "response with the correct answer"
        def quizAnswersDto = response.data
        quizAnswersDto.correctSequence.size() == 1
        quizAnswersDto.quizAnswers.size() == 1
        quizAnswersDto.quizAnswers.get(0).questionAnswers.size() == 1
        quizAnswersDto.quizAnswers.get(0).questionAnswers.get(0).question.questionDetailsDto.answer == ANSWER_1_CONTENT
        quizAnswersDto.quizAnswers.get(0).questionAnswers.get(0).answerDetails.answer == null
    }

    def "get answers of a open ended quiz as teacher without permission"(){
        given: "a teacher without permission"
        loginAsTeacher2()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ openEndedQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "get answers of a open ended quiz as student"(){
        given: "a student without permission"
        loginAsStudent()

        and: 'an empty answer to the quiz'
        def openEndedQuizAnswer = new QuizAnswer()
        openEndedQuizAnswer.setAnswerDate(DateHandler.now())
        openEndedQuizAnswer.setCompleted(true)
        openEndedQuizAnswer.setUser(student)
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
                path: "/quizzes/"+ openEndedQuiz.getId() +"/answers" ,
                requestContentType: 'application/json'
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }
}