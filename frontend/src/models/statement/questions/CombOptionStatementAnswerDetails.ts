export default class CombOptionStatementAnswerDetails {
    optionId: number | null = null;
    link: number[] = [];
  
    constructor(jsonObj?: CombOptionStatementAnswerDetails) {
      if (jsonObj) {
        this.optionId = jsonObj.optionId || this.optionId;
        this.link = jsonObj.link;
      }
    }
  }