package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeFillInQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeOrderQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenEndedQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*
import spock.lang.Unroll

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage

@DataJpaTest
class CreateQuestionTest extends SpockTest {
    def setup() {
        createExternalCourseAndExecution()
    }

    @Unroll
    def "invalid question: imageURL=#imageURL | title=#title | content=#content || errorMessage"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(title)
        questionDto.setContent(content)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        and: 'an image'
        def image = new ImageDto()
        image.setUrl(imageURL)
        image.setWidth(20)
        questionDto.setImage(image)
        and: 'two options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == errorMessage

        where:
        imageURL    | title            | content            || errorMessage
        "         " | QUESTION_1_TITLE | QUESTION_1_CONTENT || ErrorMessage.INVALID_URL_FOR_IMAGE
        IMAGE_1_URL | "              " | QUESTION_1_CONTENT || ErrorMessage.INVALID_TITLE_FOR_QUESTION
        IMAGE_1_URL | QUESTION_1_TITLE | "                " || ErrorMessage.INVALID_CONTENT_FOR_QUESTION
    }

    def "create a multiple choice question with no image and one option"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetails().getOptions().size() == 1
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
        def resOption = result.getQuestionDetails().getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()
    }

    @Unroll
    def "valid multiple choice question: ordered=#ordered | oneCorrect=#oneCorrect | oneOrder=#oneOrder | twoCorrect=#twoCorrect | twoOrder=#twoOrder |threeCorrect=#threeCorrect | threeOrder=#threeOrder || errorMessage"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOrdered(ordered)
        and: 'a image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)
        and: '3 options'
        def options = new ArrayList<OptionDto>()
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(oneCorrect)
        optionDto.setOrder(oneOrder)
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(twoCorrect)
        optionDto.setOrder(twoOrder)
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(threeCorrect)
        optionDto.setOrder(threeOrder)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getOptions().size() == 3
        result.getQuestionDetails().isOrdered() == ordered
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
        and: 'correct image'
        result.getImage().getId() != null
        result.getImage().getUrl() == IMAGE_1_URL
        result.getImage().getWidth() == 20
        and: 'correct options'
        def resOptionOne = result.getQuestionDetails().getOptions().get(0)
        resOptionOne.getContent() == OPTION_1_CONTENT
        resOptionOne.isCorrect() == oneCorrect
        resOptionOne.getOrder() == oneOrder
        def resOptionTwo = result.getQuestionDetails().getOptions().get(1)
        resOptionTwo.getContent() == OPTION_2_CONTENT
        resOptionTwo.isCorrect() == twoCorrect
        resOptionTwo.getOrder() == twoOrder
        def resOptionThree = result.getQuestionDetails().getOptions().get(2)
        resOptionThree.getContent() == OPTION_3_CONTENT
        resOptionThree.isCorrect() == threeCorrect
        resOptionThree.getOrder() == threeOrder

        where:
        ordered | oneCorrect | oneOrder | twoCorrect | twoOrder | threeCorrect | threeOrder
        false   | true       | null     | false      | null     | false        | null
        false   | true       | null     | true       | null     | true         | null
        true    | true       | 1        | true       | 2        | false        | null
    }

    @Unroll
    def "invalid multiple choice question: ordered=#ordered | oneCorrect=#oneCorrect | oneOrder=#oneOrder | twoCorrect=#twoCorrect | twoOrder=#twoOrder |threeCorrect=#threeCorrect | threeOrder=#threeOrder || errorMessage"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOrdered(ordered)
        and: '3 options'
        def options = new ArrayList<OptionDto>()
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(oneCorrect)
        optionDto.setOrder(oneOrder)
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(twoCorrect)
        optionDto.setOrder(twoOrder)
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(threeCorrect)
        optionDto.setOrder(threeOrder)
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == errorMessage

        where:
        ordered | oneCorrect | oneOrder | twoCorrect | twoOrder | threeCorrect | threeOrder || errorMessage
        false   | false      | null     | false      | null     | false        | null       || ErrorMessage.ONE_CORRECT_OPTION_NEEDED
        false   | true       | 1        | false      | null     | false        | null       || ErrorMessage.NEED_ORDERED_FLAG
        true    | true       | 1        | false      | null     | false        | null       || ErrorMessage.TWO_CORRECT_ORDERED_OPTION_NEEDED
        true    | true       | 1        | false      | 2        | false        | null       || ErrorMessage.WRONG_OPTION_WITH_ORDER
        true    | true       | null     | true       | 1        | false        | null       || ErrorMessage.CORRECT_OPTION_WITHOUT_ORDER
        true    | true       | 1        | true       | 1        | true         | 2          || ErrorMessage.INVALID_OPTIONS_ORDER
        true    | true       | 1        | true       | 2        | true         | 4          || ErrorMessage.INVALID_OPTIONS_ORDER
    }
    
    def "create a combination item question"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def combQuestionDto = new CombinationItemQuestionDto()

        questionDto.setQuestionDetailsDto(combQuestionDto)

        when:"both the sets are equal and empty"
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }

    def "create a combination question 2-to-2 with 2 connections"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def combQuestionDto = new CombinationItemQuestionDto()

        CombOptionDto combDto1 = new CombOptionDto()
        combDto1.content = "content"
        combDto1.link  = new ArrayList<Integer>();
        combDto1.left = true;

        CombOptionDto combDto2 = new CombOptionDto()
        combDto2.content = "content1"
        combDto2.link  = new ArrayList<Integer>();
        combDto2.left = true;

        CombOptionDto combDto3 = new CombOptionDto()
        combDto3.content = "content2"
        combDto3.link  = new ArrayList<Integer>();
        combDto3.left = false;

        CombOptionDto combDto4 = new CombOptionDto()
        combDto4.link  = new ArrayList<Integer>();
        combDto4.content = "content3"
        combDto4.left = false;

        combDto1.addToLink(combDto3.getId());
        combDto2.addToLink(combDto4.getId());
        combDto3.addToLink(combDto1.getId());
        combDto4.addToLink(combDto2.getId());

        combQuestionDto.getOptions().add(combDto1)
        combQuestionDto.getOptions().add(combDto2)
        combQuestionDto.getOptions().add(combDto3)
        combQuestionDto.getOptions().add(combDto4)

        questionDto.setQuestionDetailsDto(combQuestionDto)

        when:"both the sets are equal and empty"
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def opt1 = result.getQuestionDetails().getOptions().get(0)
        def opt2 = result.getQuestionDetails().getOptions().get(1)
        def link1 = result.getQuestionDetails().getOptions().get(2)
        def link2 = result.getQuestionDetails().getOptions().get(3)


        opt1.getLink().get(0) == combDto3.getId()
        opt2.getLink().get(0) == combDto4.getId()
        link1.getLink().get(0) == combDto1.getId()
        link2.getLink().get(0) == combDto2.getId()
        opt1.getContent() == "content"
        link1.getContent() == "content2"
        opt2.getContent() == "content1"
        link2.getContent() == "content3"
        opt1.isLeft() == opt2.isLeft()
        link1.isLeft() == link2.isLeft()
    }


    def "create a combination question 1-to-1 with 0 connections"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def combQuestionDto = new CombinationItemQuestionDto()

        CombOptionDto combDto1 = new CombOptionDto()
        combDto1.content = "option"
        combDto1.link  = new ArrayList<Integer>();
        combDto1.addToLink(-1);
        combDto1.left = true;

        CombOptionDto combDto2 = new CombOptionDto()
        combDto2.content = "link"
        combDto2.link  = new ArrayList<Integer>();
        combDto2.addToLink(-1);
        combDto2.left = false;

        combQuestionDto.getOptions().add(combDto1)
        combQuestionDto.getOptions().add(combDto2)


        questionDto.setQuestionDetailsDto(combQuestionDto)

        when:"both the sets are equal and empty"
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def opt = result.getQuestionDetails().getOptions().get(0)
        def link = result.getQuestionDetails().getOptions().get(1)

        opt.getLink().get(0) == -1
        link.getLink().get(0) == -1
        opt.getContent() == "option"
        link.getContent() == "link"
        opt.isLeft() == true
        link.isLeft() == false

    }

    def "create two multiple choice questions"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.getQuestionDetailsDto().setOptions(options)

        when: 'are created two questions'
        questionService.createQuestion(externalCourse.getId(), questionDto)
        questionDto.setKey(null)
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the two questions are created with the correct numbers"
        questionRepository.count() == 2L
        def resultOne = questionRepository.findAll().get(0)
        def resultTwo = questionRepository.findAll().get(1)
        resultOne.getKey() + resultTwo.getKey() == 3
    }


    def "create a code fill in question"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def codeQuestionDto = new CodeFillInQuestionDto()
        codeQuestionDto.setCode(CODE_QUESTION_1_CODE)
        codeQuestionDto.setLanguage(CODE_QUESTION_1_LANGUAGE)

        CodeFillInSpotDto fillInSpotDto = new CodeFillInSpotDto()
        OptionDto optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        fillInSpotDto.getOptions().add(optionDto)
        fillInSpotDto.setSequence(1)

        codeQuestionDto.getFillInSpots().add(fillInSpotDto)

        questionDto.setQuestionDetailsDto(codeQuestionDto)

        when:
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct data is sent back"
        rawResult instanceof QuestionDto
        def result = (QuestionDto) rawResult
        result.getId() != null
        result.getStatus() == Question.Status.AVAILABLE.toString()
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetailsDto().getFillInSpots().size() == 1
        result.getQuestionDetailsDto().getFillInSpots().get(0).getOptions().size() == 1

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def repoResult = questionRepository.findAll().get(0)
        repoResult.getId() != null
        repoResult.getKey() == 1
        repoResult.getStatus() == Question.Status.AVAILABLE
        repoResult.getTitle() == QUESTION_1_TITLE
        repoResult.getContent() == QUESTION_1_CONTENT
        repoResult.getImage() == null
        repoResult.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(repoResult)

        def repoCode = (CodeFillInQuestion) repoResult.getQuestionDetails()
        repoCode.getFillInSpots().size() == 1
        repoCode.getCode() == CODE_QUESTION_1_CODE
        repoCode.getLanguage() == CODE_QUESTION_1_LANGUAGE
        def resOption = repoCode.getFillInSpots().get(0).getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()

    }

    def "cannot create a code fill in question without fillin spots"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new CodeFillInQuestionDto())

        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_OPTION_NEEDED
    }

    def "cannot create a code fill in question with fillin spots without options"() {
        given: "a questionDto with 1 fill in spot without options"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new CodeFillInQuestionDto())

        CodeFillInSpotDto fillInSpotDto = new CodeFillInSpotDto()
        questionDto.getQuestionDetailsDto().getFillInSpots().add(fillInSpotDto)


        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.NO_CORRECT_OPTION
    }

    def "cannot create a code fill in question with fillin spots without correct options"() {
        given: "a questionDto with 1 fill in spot without options"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new CodeFillInQuestionDto())

        CodeFillInSpotDto fillInSpotDto = new CodeFillInSpotDto()
        OptionDto optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(false)
        questionDto.getQuestionDetailsDto().getFillInSpots().add(fillInSpotDto)


        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.NO_CORRECT_OPTION
    }


    def "create a code order question"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def codeQuestionDto = new CodeOrderQuestionDto()
        codeQuestionDto.setLanguage(CODE_QUESTION_1_LANGUAGE)

        CodeOrderSlotDto slotDto1 = new CodeOrderSlotDto()
        slotDto1.content = OPTION_1_CONTENT;
        slotDto1.order = 1;

        CodeOrderSlotDto slotDto2 = new CodeOrderSlotDto()
        slotDto2.content = OPTION_1_CONTENT;
        slotDto2.order = 2;

        CodeOrderSlotDto slotDto3 = new CodeOrderSlotDto()
        slotDto3.content = OPTION_1_CONTENT;
        slotDto3.order = 3;

        codeQuestionDto.getCodeOrderSlots().add(slotDto1)
        codeQuestionDto.getCodeOrderSlots().add(slotDto2)
        codeQuestionDto.getCodeOrderSlots().add(slotDto3)

        questionDto.setQuestionDetailsDto(codeQuestionDto)

        when:
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct data is sent back"
        rawResult instanceof QuestionDto
        def result = (QuestionDto) rawResult
        result.getId() != null
        result.getStatus() == Question.Status.AVAILABLE.toString()
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetailsDto().getCodeOrderSlots().size() == 3
        result.getQuestionDetailsDto().getCodeOrderSlots().get(0).getContent() == OPTION_1_CONTENT

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def repoResult = questionRepository.findAll().get(0)
        repoResult.getId() != null
        repoResult.getKey() == 1
        repoResult.getStatus() == Question.Status.AVAILABLE
        repoResult.getTitle() == QUESTION_1_TITLE
        repoResult.getContent() == QUESTION_1_CONTENT
        repoResult.getImage() == null
        repoResult.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(repoResult)

        def repoCode = (CodeOrderQuestion) repoResult.getQuestionDetails()
        repoCode.getCodeOrderSlots().size() == 3
        repoCode.getLanguage() == CODE_QUESTION_1_LANGUAGE
        def resOption = repoCode.getCodeOrderSlots().get(0)
        resOption.getContent() == OPTION_1_CONTENT
    }

    def "cannot create a code order question without CodeOrderSlots"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def codeQuestionDto = new CodeOrderQuestionDto()
        codeQuestionDto.setLanguage(CODE_QUESTION_1_LANGUAGE)

        questionDto.setQuestionDetailsDto(codeQuestionDto)

        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_THREE_SLOTS_NEEDED
    }

    def "cannot create a code order question without 3 CodeOrderSlots"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def codeQuestionDto = new CodeOrderQuestionDto()
        codeQuestionDto.setLanguage(CODE_QUESTION_1_LANGUAGE)

        CodeOrderSlotDto slotDto1 = new CodeOrderSlotDto()
        slotDto1.content = OPTION_1_CONTENT;
        slotDto1.order = 1;

        CodeOrderSlotDto slotDto2 = new CodeOrderSlotDto()
        slotDto2.content = OPTION_1_CONTENT;
        slotDto2.order = 2;

        codeQuestionDto.getCodeOrderSlots().add(slotDto1)
        codeQuestionDto.getCodeOrderSlots().add(slotDto2)

        questionDto.setQuestionDetailsDto(codeQuestionDto)
        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_THREE_SLOTS_NEEDED
    }

    def "cannot create a code order question without 3 CodeOrderSlots with order"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def codeQuestionDto = new CodeOrderQuestionDto()
        codeQuestionDto.setLanguage(CODE_QUESTION_1_LANGUAGE)

        CodeOrderSlotDto slotDto1 = new CodeOrderSlotDto()
        slotDto1.content = OPTION_1_CONTENT;
        slotDto1.order = 1;

        CodeOrderSlotDto slotDto2 = new CodeOrderSlotDto()
        slotDto2.content = OPTION_1_CONTENT;
        slotDto2.order = 2;

        CodeOrderSlotDto slotDto3 = new CodeOrderSlotDto()
        slotDto3.content = OPTION_1_CONTENT;
        slotDto3.order = null;

        codeQuestionDto.getCodeOrderSlots().add(slotDto1)
        codeQuestionDto.getCodeOrderSlots().add(slotDto2)
        codeQuestionDto.getCodeOrderSlots().add(slotDto3)

        questionDto.setQuestionDetailsDto(codeQuestionDto)
        when:
        def result = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_THREE_SLOTS_NEEDED
    }

    def "create open ended question"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def openEndedQuestionDto = new OpenEndedQuestionDto()
        openEndedQuestionDto.setAnswer(ANSWER_1_CONTENT)

        questionDto.setQuestionDetailsDto(openEndedQuestionDto)

        when:
        def rawResult = questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct data is sent back"
        rawResult instanceof QuestionDto
        def result = (QuestionDto) rawResult
        result.getId() != null
        result.getStatus() == Question.Status.AVAILABLE.toString()
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def repoResult = questionRepository.findAll().get(0)
        repoResult.getId() != null
        repoResult.getKey() == 1
        repoResult.getStatus() == Question.Status.AVAILABLE
        repoResult.getTitle() == QUESTION_1_TITLE
        repoResult.getContent() == QUESTION_1_CONTENT
        repoResult.getImage() == null
        repoResult.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(repoResult)
        repoResult.getQuestionDetails().getAnswer() == ANSWER_1_CONTENT

    }

    @Unroll
    def "fail to create any question for invalid/non-existent course (#nonExistentId)"(Integer nonExistentId) {
        given: "any multiple choice question dto"
        def questionDto = new QuestionDto()
        when:
        questionService.createQuestion(nonExistentId, questionDto)
        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.COURSE_NOT_FOUND
        where:
        nonExistentId << [-1, 0, 200]
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
