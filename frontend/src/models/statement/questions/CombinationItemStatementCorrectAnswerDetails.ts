import CombinationItemAnswerOption from '@/models/management/questions/CombinationItemAnswerOption';
import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class CombinationItemStatementCorrectAnswerDetails extends StatementCorrectAnswerDetails {
  public correctOptions!: CombinationItemAnswerOption[];

  constructor(jsonObj?: CombinationItemStatementCorrectAnswerDetails) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      this.correctOptions = jsonObj.correctOptions || [];
    }
  }
}