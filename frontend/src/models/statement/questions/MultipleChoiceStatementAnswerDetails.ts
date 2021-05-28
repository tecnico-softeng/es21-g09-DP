import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import MultipleChoiceStatementCorrectAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementCorrectAnswerDetails';
import OptionStatementAnswerDetails from '@/models/statement/questions/OptionStatementAnswerDetails';

export default class MultipleChoiceStatementAnswerDetails extends StatementAnswerDetails {
  public answeredOptions!: OptionStatementAnswerDetails[];

  constructor(jsonObj?: MultipleChoiceStatementAnswerDetails) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.answeredOptions = jsonObj.answeredOptions || [];
    }
  }

  isQuestionAnswered(): boolean {
    return this.answeredOptions != null && this.answeredOptions.length > 0;
  }

  isAnswerCorrect(
    correctAnswerDetails: MultipleChoiceStatementCorrectAnswerDetails
  ): boolean {
    var correctAnswers=correctAnswerDetails.correctOptions.filter(x => x.correct);
    if(correctAnswers.length !== this.answeredOptions.length){  
      return false;
    }
    for (const key in correctAnswers) {
      const correct = correctAnswers[key];
      if(!this.answeredOptions.some(function(e){
          return e.optionId == correct.optionId && e.order == correct.order
        })){
        return false;
      }
    }
    return true;
  }
}
