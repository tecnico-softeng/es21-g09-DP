import CombOption from '@/models/management/questions/CombOption';
import QuestionDetails from '@/models/management/questions/QuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class CombinationItemQuestionDetails extends QuestionDetails {
  localId: number = 0;
  options: CombOption[] = [new CombOption(), new CombOption(), new CombOption(), new CombOption()];


  constructor(jsonObj?: CombinationItemQuestionDetails) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      this.options = jsonObj.options.map(
        (combOption: CombOption) => new CombOption(combOption)
      );
    }
  }

  setAsNew(): void {
    this.options.forEach((combOption) => {
        combOption.id = null;
    });
  }
}
