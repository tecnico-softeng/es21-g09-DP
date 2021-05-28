export default class OptionStatementAnswerDetails {
    optionId: number | null = null;
    order: number | null = null;
  
    constructor(jsonObj?: OptionStatementAnswerDetails) {
      if (jsonObj) {
        this.optionId = jsonObj.optionId || this.optionId;
        this.order = jsonObj.order || this.order;
      }
    }
  }