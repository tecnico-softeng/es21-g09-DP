import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import CombinationItemStatementCorrectAnswerDetails from '@/models/statement/questions/CombinationItemStatementCorrectAnswerDetails';
import CombOptionStatementAnswerDetails from '@/models/statement/questions/CombOptionStatementAnswerDetails';

export default class CombinationItemStatementAnswerDetails extends StatementAnswerDetails {
  public answeredOptions!: CombOptionStatementAnswerDetails[];

  constructor(jsonObj?: CombinationItemStatementAnswerDetails) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      this.answeredOptions = jsonObj.answeredOptions || [];
    }
  }

  isQuestionAnswered(): boolean {
    return this.answeredOptions != null && this.answeredOptions.length > 0;
  }

  isAnswerCorrect(correctAnswerDetails: CombinationItemStatementCorrectAnswerDetails): boolean {
    for(let combOption of correctAnswerDetails.correctOptions){
        for(let answeredOption of this.answeredOptions){
            if(combOption.id == answeredOption.optionId){
                if(combOption.link != answeredOption.link){
                    return false;
                }
            }
        }
    };

    return true;
  }
}