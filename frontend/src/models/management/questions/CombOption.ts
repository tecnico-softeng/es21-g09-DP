export default class CombOption {
    id: number | null = null;
    sequence!: number | null;
    content: string = '';
    link: number[] = [];
    left: boolean = false;
    localId: number | undefined;
  
    constructor(jsonObj?: CombOption) {
      if (jsonObj) {
        this.id = jsonObj.id;
        this.link = jsonObj.link;
        this.sequence = jsonObj.sequence;
        this.content = '';
        this.left = jsonObj.left;
      }
    }
  }
