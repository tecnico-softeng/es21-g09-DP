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



class ExportQuizAnswersWebServiceIT extends QuizWebServiceIT {
    def "export a multiple choice quiz as teacher with permission"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }

    def "export a multiple choice quiz as teacher without permission"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }

    def "export a multiple choice quiz as student"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ multipleChoiceQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }

    def "export a open ended quiz as teacher with permission"(){
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
        def answerDetails = new OpenEndedAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }

    def "export a open ended quiz as teacher without permission"(){
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
        def answerDetails = new OpenEndedAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }

    def "export a open ended quiz as student"(){
        given: "a student"
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
        def answerDetails = new OpenEndedAnswer(questionAnswer);
        questionAnswer.setAnswerDetails(answerDetails);

        quizAnswerRepository.save(openEndedQuizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ openEndedQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }

    def "export a combination item quiz as teacher with permission"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }

    def "export a combination item quiz as teacher without permission"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }
    def "export a combination item quiz as student"(){
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

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: "the web service is invoked"
        def map = restClient.get(
                path: "/quizzes/"+ combinationItemQuiz.getId() +"/export",
                requestContentType: 'application/json'
        )

        then: "the response status is OK"
        assert map['response'].status == 403
    }
    

}