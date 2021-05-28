export default class combinationItemAnswerOption{
    id: number | null = null;
    sequence!: number | null;
    content: string = '';
    link: number[] = [];
    left: boolean = false;
    localId: number | undefined;
  
    constructor(jsonObj?: combinationItemAnswerOption) {
      if (jsonObj) {
        this.id = jsonObj.id;
        this.link = jsonObj.link;
        this.sequence = jsonObj.sequence;
        this.content = jsonObj.content;
        this.left = jsonObj.left;
      }
    }
  }