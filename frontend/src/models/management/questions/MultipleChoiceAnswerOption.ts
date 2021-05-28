export default class multipleChoiceAnswerOption {
    optionId: number | null = null;
    order: number | null = null;
    correct: boolean = false;
    sequence: number | null = null;
  
    constructor(jsonObj?: multipleChoiceAnswerOption) {
      if (jsonObj) {
        this.optionId = jsonObj.optionId;
        this.order = jsonObj.order;
        this.correct = jsonObj.correct;
        this.sequence = jsonObj.sequence;
      }
    }
  }