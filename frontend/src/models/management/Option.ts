export default class Option {
  id: number | null = null;
  sequence!: number | null;
  content: string = '';
  correct: boolean = false;
  order: number | null = null;
  localId: number | undefined;

  constructor(jsonObj?: Option) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.order = jsonObj.order;
      this.sequence = jsonObj.sequence;
      this.content = '';
      this.correct = jsonObj.correct;
    }
  }
}
