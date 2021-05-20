import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import StatementOption from '@/models/statement/StatementOption';
import { _ } from 'vue-underscore';

export default class CombinationItemStatementQuestionDetails extends StatementQuestionDetails {
  options: StatementOption[] = [];

  constructor(jsonObj?: CombinationItemStatementQuestionDetails) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      if (jsonObj.options) {
        this.options = _.shuffle(
          jsonObj.options.map(
            (option: StatementOption) => new StatementOption(option)
          )
        );
      }
    }
  }
}
