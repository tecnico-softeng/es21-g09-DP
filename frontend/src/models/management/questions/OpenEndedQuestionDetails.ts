import QuestionDetails from '@/models/management/questions/QuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenEndedQuestionDetails extends QuestionDetails {
    answer: string = '';

    constructor(jsonObj?: OpenEndedQuestionDetails) {
        super(QuestionTypes.OpenEnded);
        if (jsonObj) {
            this.answer = jsonObj.answer;
        }
    }

    setAsNew(): void {

    }

}
