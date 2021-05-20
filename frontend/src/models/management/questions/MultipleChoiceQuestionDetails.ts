import Option from '@/models/management/Option';
import QuestionDetails from '@/models/management/questions/QuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class MultipleChoiceQuestionDetails extends QuestionDetails {
  localId: number = 0;
  ordered: boolean = false;
  options: Option[] = [new Option(), new Option(), new Option(), new Option()];

  constructor(jsonObj?: MultipleChoiceQuestionDetails) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.ordered = jsonObj.ordered
      this.options = jsonObj.options.map(
        (option: Option) => new Option(option)
      );
    }
  }

  setAsNew(): void {
    this.options.forEach((option) => {
      option.id = null;
    });
  }
}
