package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Image
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombinationItemQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombinationItemQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenEndedQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CombOption
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CombOptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User

@DataJpaTest
class UpdateQuestionTest extends SpockTest {
    def question
    def nquestion
    def optionOK
    def optionKO
    def combOptionOK
    def combOptionKO
    def user


    def setup() {
        createExternalCourseAndExecution()

        user = new User(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        user.addCourse(externalCourseExecution)
        userRepository.save(user)

        and: 'an image'
        def image = new Image()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        imageRepository.save(image)

        given: "create a question"
        question = new Question()
        question.setCourse(externalCourse)
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setStatus(Question.Status.AVAILABLE)
        question.setNumberOfAnswers(2)
        question.setNumberOfCorrect(1)
        question.setImage(image)

        nquestion = new Question()
        nquestion.setCourse(externalCourse)
        nquestion.setKey(1)
        nquestion.setTitle(QUESTION_2_TITLE)
        nquestion.setContent(QUESTION_1_CONTENT)
        nquestion.setStatus(Question.Status.AVAILABLE)
        nquestion.setImage(image)

        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        def combQuestionDetails = new CombinationItemQuestion()
        nquestion.setQuestionDetails(combQuestionDetails)
        questionDetailsRepository.save(combQuestionDetails)
        questionRepository.save(nquestion)

        and: 'four options'
        optionOK = new Option()
        optionOK.setContent(OPTION_1_CONTENT)
        optionOK.setCorrect(true)
        optionOK.setSequence(0)
        optionOK.setQuestionDetails(questionDetails)
        optionRepository.save(optionOK)

        optionKO = new Option()
        optionKO.setContent(OPTION_2_CONTENT)
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(questionDetails)
        optionRepository.save(optionKO)

        combOptionOK = new CombOption()
        combOptionOK.setContent(OPTION_1_CONTENT)
        combOptionOK.setLeft(true)
        combOptionOK.addToLink(1)
        combOptionOK.setSequence(0)
        combOptionOK.setQuestionDetails(combQuestionDetails)
        optionRepository.save(combOptionOK)

        combOptionKO = new CombOption()
        combOptionKO.setContent(OPTION_2_CONTENT)
        combOptionKO.setLeft(false)
        combOptionKO.addToLink(1)
        combOptionKO.setSequence(1)
        combOptionKO.setQuestionDetails(combQuestionDetails)
        optionRepository.save(combOptionKO)
    }

    def "updates a combination item question with options"(){
        given: "a changed question"
        def questionDto = new QuestionDto(nquestion)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setQuestionDetailsDto(new CombinationItemQuestionDto())
        and: '2 changed combOptions'
        def combOptions = new ArrayList<CombOptionDto>()
        def combOptionDto = new CombOptionDto(combOptionOK)
        combOptionDto.removeFromLink(1)
        combOptionDto.addToLink(2)
        combOptions.add(combOptionDto)
        combOptionDto = new CombOptionDto(combOptionKO)
        combOptionDto.removeFromLink(1)
        combOptionDto.addToLink(2)
        combOptions.add(combOptionDto)

        questionDto.getQuestionDetailsDto().setOptions(combOptions)
        and: 'a count to load options to memory due to in memory database flaw'
        optionRepository.count();

        when:
        questionService.updateQuestion(nquestion.getId(), questionDto)

        then: "the question is changed"
        questionRepository.count() == 2L
        def result = questionRepository.findAll().get(1)
        result.getId() == nquestion.getId()
        result.getTitle() == QUESTION_2_TITLE
        result.getContent() == QUESTION_2_CONTENT

        and: 'are not changed'
        result.getStatus() == Question.Status.AVAILABLE

        and: 'both Sets are changed'
        def opt1 = result.getQuestionDetails().getOptions().get(0)
        opt1.isInLink(2) == true
        opt1.isLeft() == true
        def opt2 = result.getQuestionDetails().getOptions().get(1)
        opt2.isInLink(2) == true
        opt2.isLeft() == false

    }



    def "update a question"() {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        and: '2 changed options'
        def options = new ArrayList<OptionDto>()
        def optionDto = new OptionDto(optionOK)
        optionDto.setCorrect(false)
        options.add(optionDto)
        optionDto = new OptionDto(optionKO)
        optionDto.setCorrect(true)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)
        and: 'a count to load options to memory due to in memory database flaw'
        optionRepository.count();

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question is changed"
        questionRepository.count() == 2L
        def result = questionRepository.findAll().get(0)
        result.getId() == question.getId()
        result.getTitle() == QUESTION_2_TITLE
        result.getContent() == QUESTION_2_CONTENT
        and: 'are not changed'
        result.getStatus() == Question.Status.AVAILABLE
        result.getNumberOfAnswers() == 2
        result.getNumberOfCorrect() == 1
        result.getDifficulty() == 50
        result.getImage() != null
        and: 'an option is changed'
        result.getQuestionDetails().getOptions().size() == 2
        def resOptionOne = result.getQuestionDetails().getOptions().stream().filter({ option -> option.isCorrect()}).findAny().orElse(null)
        resOptionOne.getContent() == OPTION_2_CONTENT
        def resOptionTwo = result.getQuestionDetails().getOptions().stream().filter({ option -> !option.isCorrect()}).findAny().orElse(null)
        resOptionTwo.getContent() == OPTION_1_CONTENT
        and: 'there are two questions in the database'
        optionRepository.findAll().size() == 2
    }

    def "update question with missing data"() {
        given: 'a question'
        def questionDto = new QuestionDto(question)
        questionDto.setTitle('     ')

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.INVALID_TITLE_FOR_QUESTION
    }

    def "update question with ordered options"() {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOrdered(true)
        and: '2 changed options'
        def options = new ArrayList<OptionDto>()
        def optionDto = new OptionDto(optionOK)
        optionDto.setCorrect(true)
        optionDto.setOrder(1)
        options.add(optionDto)
        optionDto = new OptionDto(optionKO)
        optionDto.setCorrect(true)
        optionDto.setOrder(2)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)
        and: 'a count to load options to memory due to in memory database flaw'
        optionRepository.count();

        when:
        questionService.updateQuestion(question.getId(), questionDto)
        
        then: 'question and options changed correctly'
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().isOrdered()
        result.getQuestionDetails().getOptions().size() == 2
        result.getQuestionDetails().getOptions().stream().filter(option -> option.getOrder() != null).count() == 2
    }

    def "update question with two options true"() {
        given: 'a question'
        def questionDto = new QuestionDto(question)
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        def optionDto = new OptionDto(optionOK)
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        optionDto = new OptionDto(optionKO)
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(true)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)
        and: 'a count to load options to memory due to in memory database flaw'
        optionRepository.count();

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question is not changed"
        questionRepository.count() == 2L
        def result = questionRepository.findAll().get(0)
        result.getId() == question.getId()
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getStatus() == Question.Status.AVAILABLE
        result.getNumberOfAnswers() == 2
        result.getNumberOfCorrect() == 1
        result.getDifficulty() == 50
        result.getImage() != null
        and: 'both options are correct'
        result.getQuestionDetails().getOptions().stream().filter({ option -> option.isCorrect()}).count() == 2
    }

    def "update correct option in a question with answers"() {
        given: "a question with answers"
        Quiz quiz = new Quiz()
        quiz.setKey(1)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quizRepository.save(quiz)

        QuizQuestion quizQuestion= new QuizQuestion()
        quizQuestion.setQuiz(quiz)
        quizQuestion.setQuestion(question)
        quizQuestionRepository.save(quizQuestion)

        def quizAnswer = new QuizAnswer()
        quizAnswer.setCompleted(true)
        quizAnswer.setUser(user)
        quizAnswer.setQuiz(quiz)
        quizAnswerRepository.save(quizAnswer)

        def questionAnswer = new QuestionAnswer()
        def answerDetails = new MultipleChoiceAnswer(questionAnswer, optionOK,null)
        questionAnswer.setAnswerDetails(answerDetails)
        questionAnswer.setQuizQuestion(quizQuestion)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)

        questionAnswer = new QuestionAnswer()
        answerDetails = new MultipleChoiceAnswer(questionAnswer, optionKO,null)
        questionAnswer.setAnswerDetails(answerDetails)
        questionAnswer.setQuizQuestion(quizQuestion)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)


        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setStatus(Question.Status.DISABLED.name())
        questionDto.setNumberOfAnswers(4)
        questionDto.setNumberOfCorrect(2)
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and: 'a optionId'
        def optionDto = new OptionDto(optionOK)
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)

        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        optionDto = new OptionDto(optionKO)
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.CANNOT_CHANGE_ANSWERED_QUESTION
    }

    def "update open ended question"(){
        given: "a question"
        def openEndedQuestion = new Question()
        openEndedQuestion.setCourse(externalCourse)
        openEndedQuestion.setKey(1)
        openEndedQuestion.setTitle(QUESTION_1_TITLE)
        openEndedQuestion.setContent(QUESTION_1_CONTENT)
        openEndedQuestion.setStatus(Question.Status.AVAILABLE)

        def openEndedQuestionDetails = new OpenEndedQuestion()
        openEndedQuestionDetails.setAnswer(ANSWER_1_CONTENT)
        openEndedQuestion.setQuestionDetails(openEndedQuestionDetails)
        questionDetailsRepository.save(openEndedQuestionDetails)
        questionRepository.save(openEndedQuestion)

        and: "change question"
        def openEndedQuestionDto = new QuestionDto(openEndedQuestion)
        openEndedQuestionDto.setTitle(QUESTION_2_TITLE)
        openEndedQuestionDto.setContent(QUESTION_2_CONTENT)
        openEndedQuestionDto.setQuestionDetailsDto(new OpenEndedQuestionDto())
        openEndedQuestionDto.getQuestionDetailsDto().setAnswer(ANSWER_2_CONTENT)

        when:
        questionService.updateQuestion(openEndedQuestion.getId(), openEndedQuestionDto)

        then: "the question is changed"
        questionRepository.count() == 3L
        def result = questionRepository.findAll().get(2)
        result.getId() == openEndedQuestion.getId()
        result.getTitle() == QUESTION_2_TITLE
        result.getContent() == QUESTION_2_CONTENT

        and: 'are not changed'
        result.getStatus() == Question.Status.AVAILABLE
        result.getImage() == null

        and: 'answer is changed'
        result.getQuestionDetails().getAnswer() == ANSWER_2_CONTENT
    }

    def "update MultipleChoiceQuestion remove old option add new one"() {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        def multipleChoiceQuestionDto = new MultipleChoiceQuestionDto()
        questionDto.setQuestionDetailsDto(multipleChoiceQuestionDto)
        and: 'a the old correct option'
        def newOptionOK = new OptionDto(optionOK)
        and: 'a new option'
        def newOptionKO = new OptionDto()
        newOptionKO.setContent(OPTION_1_CONTENT)
        newOptionKO.setCorrect(false)
        and: 'add options to dto'
        def newOptions = new ArrayList<OptionDto>()
        newOptions.add(newOptionOK)
        newOptions.add(newOptionKO)
        multipleChoiceQuestionDto.setOptions(newOptions)
        and: 'a count to load options to memory due to in memory database flaw'
        optionRepository.count();

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question is there"
        questionRepository.count() == 2L
        def result = questionRepository.findAll().get(0)
        and: 'an option is changed'
        result.getQuestionDetails().getOptions().size() == 2
        def resOptionOne = result.getQuestionDetails().getOptions().stream().filter({ option -> option.isCorrect()}).findAny().orElse(null)
        resOptionOne.getContent() == OPTION_1_CONTENT
        def resOptionTwo = result.getQuestionDetails().getOptions().stream().filter({ option -> !option.isCorrect()}).findAny().orElse(null)
        resOptionTwo.getContent() == OPTION_1_CONTENT
        and: 'there are two questions in the database'
        optionRepository.findAll().size() == 2
    }
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}