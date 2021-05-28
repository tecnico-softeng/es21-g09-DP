export default class StatementCombOption {
    optionId!: number;
    link!: number[];
  
    constructor(jsonObj?: StatementCombOption) {
      if (jsonObj) {
        this.optionId = jsonObj.optionId;
        this.link = jsonObj.link;
      }
    }
  }
  