package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseException
import com.fasterxml.jackson.databind.ObjectMapper

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto

class UpdateQuestionWebServiceIT extends QuestionWebServiceIT {

    def multiChoiceQuestion
    def openEndedQuestion
    def comboItemQuestion

    def setup() {
        questionService.createQuestion(course.getId(), comboItemQuestionDto)
        comboItemQuestion = questionRepository.findAll().get(0)
        
        questionService.createQuestion(course.getId(), openEndedQuestionDto)
        openEndedQuestion = questionRepository.findAll().get(1)

        questionService.createQuestion(course.getId(), multiChoiceQuestionDto)
        multiChoiceQuestion = questionRepository.findAll().get(2)
    }
    
    def "update open-ended question as a teacher"() {
        given: "a teacher"
        loginAsTeacher()
        and: "a questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new OpenEndedQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        newQuestionDto.getQuestionDetailsDto().setAnswer(ANSWER_2_CONTENT)

        when: "the question is changed"
        def mapper = new ObjectMapper()
        response = restClient.put(
                path: "/questions/" + openEndedQuestion.getId(),
                body: mapper.writeValueAsString(newQuestionDto),
                requestContentType: "application/json"
        )

        then: "check response status"
        response != null
        response.status == 200
        and: "correct question"
        def question = response.data
        question.id != null
        question.title == newQuestionDto.getTitle()
        question.content == newQuestionDto.getContent()
        question.status == Question.Status.AVAILABLE.name()
        question.questionDetailsDto.answer == ANSWER_2_CONTENT
    }

    def "update open-ended question as a student"() {
        given: "a student"
        loginAsStudent()
        and: "a questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new OpenEndedQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        newQuestionDto.getQuestionDetailsDto().setAnswer(ANSWER_2_CONTENT)

        when: "the question is changed"
        def mapper = new ObjectMapper()
        
        response = restClient.put(
                path: "/questions/" + openEndedQuestion.getId(),
                body: mapper.writeValueAsString(newQuestionDto),
                requestContentType: "application/json"
        )

        then: "exception is thrown"
        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "update multiple choice question as teacher"(){
        given: "a teacher"
        loginAsTeacher()

        and: "a new multiple choice questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        def newOptionDto = new OptionDto(optionRepository.findAll().get(0))
        newOptionDto.setContent(OPTION_2_CONTENT)
        newOptionDto.setCorrect(true)
        def newOptions = new ArrayList<OptionDto>()
        newOptions.add(newOptionDto)
        newQuestionDto.getQuestionDetailsDto().setOptions(newOptions)


        when: "the web service is invoked"
        response = restClient.put(
                path: '/questions/'+ multiChoiceQuestion.getId() ,
                body: JsonOutput.toJson(newQuestionDto),
                requestContentType: 'application/json'
        )

        then: "check response status "
        response != null
        response.status == 200

        and: "if it responds with the correct question"
        def updatedQuestionDto = response.data
        updatedQuestionDto.id != null
        updatedQuestionDto.status == newQuestionDto.getStatus()
        updatedQuestionDto.title == newQuestionDto.getTitle()
        updatedQuestionDto.content == newQuestionDto.getContent()
        !updatedQuestionDto.questionDetailsDto.ordered
        updatedQuestionDto.questionDetailsDto.options.get(0).content == newOptionDto.getContent()
    }

    def "update multiple choice question as student"(){
        given: "a student"
        loginAsStudent()

        and: "a new multiple choice questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())
        def newOptionDto = new OptionDto(optionRepository.findAll().get(0))
        newOptionDto.setContent(OPTION_2_CONTENT)
        newOptionDto.setCorrect(true)
        def newOptions = new ArrayList<OptionDto>()
        newOptions.add(newOptionDto)
        newQuestionDto.getQuestionDetailsDto().setOptions(newOptions)

        when: "the web service is invoked"
        response = restClient.put(
                path: '/questions/'+ multiChoiceQuestion.getId() ,
                body: JsonOutput.toJson(newQuestionDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"

        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }

    def "update combination-item question as teacher"(){
        given: "a teacher"
        loginAsTeacher()

        and: "a new combination-item questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new CombinationItemQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())

        def newCombOptionDto = new CombOptionDto()
        newCombOptionDto.setContent(OPTION_1_CONTENT)
        newCombOptionDto.link.add(2)
        newCombOptionDto.setLeft(false)
        def newCombOptions = new ArrayList<CombOptionDto>()
        newCombOptions.add(newCombOptionDto)
        newQuestionDto.getQuestionDetailsDto().setOptions(newCombOptions)

        def mapper = new ObjectMapper()
        when: "the web service is invoked"
        response = restClient.put(
                path: '/questions/'+ comboItemQuestion.getId() ,
                body: mapper.writeValueAsString(newQuestionDto),
                requestContentType: 'application/json'
        )
        then: "check response status"
        response != null
        response.status == 200

        and: "if it responds with the correct question"
        def updatedQuestionDto = response.data
        updatedQuestionDto.id != null
        updatedQuestionDto.status == newQuestionDto.getStatus()
        updatedQuestionDto.title == newQuestionDto.getTitle()
        updatedQuestionDto.content == newQuestionDto.getContent()
        updatedQuestionDto.questionDetailsDto.options.get(0).link.get(0) == newCombOptionDto.getLink().get(0)
        updatedQuestionDto.questionDetailsDto.options.get(0).content == newCombOptionDto.getContent()
        updatedQuestionDto.questionDetailsDto.options.get(0).left == newCombOptionDto.isLeft()
    }

    def "update combination-item choice question as student"(){
        given: "a student"
        loginAsStudent()

        and: "a new combination-item choice questionDto"
        def newQuestionDto = new QuestionDto()
        newQuestionDto.setQuestionDetailsDto(new CombinationItemQuestionDto())
        newQuestionDto.setTitle(QUESTION_2_TITLE)
        newQuestionDto.setContent(QUESTION_2_CONTENT)
        newQuestionDto.setStatus(Question.Status.AVAILABLE.name())

        def newCombOptionDto = new CombOptionDto()
        newCombOptionDto.setContent(OPTION_1_CONTENT)
        newCombOptionDto.link.add(2)
        newCombOptionDto.setLeft(false)
        def newCombOptions = new ArrayList<CombOptionDto>()
        newCombOptions.add(newCombOptionDto)
        newQuestionDto.getQuestionDetailsDto().setOptions(newCombOptions)

        when: "the web service is invoked"
        response = restClient.put(
                path: '/questions/'+ comboItemQuestion.getId() ,
                body: JsonOutput.toJson(newQuestionDto),
                requestContentType: 'application/json'
        )

        then: "exception is thrown"

        def exception = thrown(HttpResponseException)
        exception.response.status == 403
    }
}