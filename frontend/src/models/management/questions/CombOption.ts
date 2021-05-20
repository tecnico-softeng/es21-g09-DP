export default class CombOption {
    id: number | null = null;
    sequence!: number | null;
    content: string = '';
    link: number = -1;
    left: boolean = false;
    localId: number | undefined;
  
    constructor(jsonObj?: CombOption) {
      if (jsonObj) {
        this.id = jsonObj.id;
        this.link = jsonObj.link;
        this.sequence = jsonObj.sequence;
        this.content = jsonObj.content;
        this.left = jsonObj.left;
      }
    }
  }
