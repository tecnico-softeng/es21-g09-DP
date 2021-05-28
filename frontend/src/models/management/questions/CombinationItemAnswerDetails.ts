import CombOption from '@/models/management/questions/CombOption';
import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes, convertToLetter } from '@/services/QuestionHelpers';
import CombinationItemAnswerOption from './CombinationItemAnswerOption';
import CombinationItemQuestionDetails from './CombinationItemQuestionDetails';

export default class CombinationItemAnswerType extends AnswerDetails {
  answeredOptions: CombinationItemAnswerOption[] = [];
  constructor(jsonObj?: CombinationItemAnswerType) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      this.answeredOptions = jsonObj.answeredOptions.map(
        (option: CombinationItemAnswerOption) =>
          new CombinationItemAnswerOption(option)
      );
    }
  }

  isCorrect(questionDetails: CombinationItemQuestionDetails): boolean {
    for(let combOption of questionDetails.options){
        for(let answeredOption of this.answeredOptions){
            if(combOption.id == answeredOption.id){
                if(combOption.link != answeredOption.link){
                    return false;
                }
            }
        }
    };

    return true;
  }

  answerRepresentation(): string {
    return this.answeredOptions.map(x=>convertToLetter(x.sequence)).join(" | ");
  }
}