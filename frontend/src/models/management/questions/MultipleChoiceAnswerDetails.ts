import Option from '@/models/management/Option';
import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes, convertToLetter } from '@/services/QuestionHelpers';
import multipleChoiceAnswerOption from './MultipleChoiceAnswerOption';
import MultipleChoiceQuestionDetails from './MultipleChoiceQuestionDetails';

export default class MultipleChoiceAnswerType extends AnswerDetails {
  answeredOptions: multipleChoiceAnswerOption[] = [];

  constructor(jsonObj?: MultipleChoiceAnswerType) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.answeredOptions = jsonObj.answeredOptions.map(
        (option: multipleChoiceAnswerOption) =>
          new multipleChoiceAnswerOption(option)
      );
    }
  }

  isCorrect(questionDetails: MultipleChoiceQuestionDetails): boolean {
    return this.answeredOptions.length ===
    questionDetails.options.filter((os) => os.correct).length && 
    this.answeredOptions.filter((os) => !os.correct).length == 0
  }
  answerRepresentation(): string {
    return this.answeredOptions.map(x=>convertToLetter(x.sequence)).join(" | ");
  }
}
