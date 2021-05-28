import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import StatementCombOption from '@/models/statement/StatementCombOption';
import { _ } from 'vue-underscore';

export default class CombinationItemStatementQuestionDetails extends StatementQuestionDetails {
  options: StatementCombOption[] = [];

  constructor(jsonObj?: CombinationItemStatementQuestionDetails) {
    super(QuestionTypes.CombinationItem);
    if (jsonObj) {
      if (jsonObj.options) {
        this.options = _.shuffle(
          jsonObj.options.map(
            (option: StatementCombOption) => new StatementCombOption(option)
          )
        );
      }
    }
  }
}
