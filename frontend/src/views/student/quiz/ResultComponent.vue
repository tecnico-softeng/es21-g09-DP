<template>
  <div
    v-if="question"
    v-bind:class="[
      'question-container',
      !answer.isQuestionAnswered() ? 'unanswered' : '',
      answer.isQuestionAnswered() && answer.isAnswerCorrect(correctAnswer)
        ? 'correct-question'
        : 'incorrect-question',
    ]"
  >
    <div class="question">
      <span
        @click="decreaseOrder"
        @mouseover="hover = true"
        @mouseleave="hover = false"
        class="square"
      >
        <i v-if="hover && questionOrder !== 0" class="fas fa-chevron-left" />
        <span v-else>{{ questionOrder + 1 }}</span>
      </span>
      <div
        class="question-content"
        v-html="convertMarkDown(question.content, question.image)"
      ></div>
      <div @click="increaseOrder" class="square">
        <i
          v-if="questionOrder !== questionNumber - 1"
          class="fas fa-chevron-right"
        />
      </div>
    </div>
    <component
      :is="questionComponent"
      :questionDetails="question.questionDetails"
      :answerDetails="answer.answerDetails"
      :correctAnswerDetails="correctAnswer.correctAnswerDetails"
    >
    </component>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Emit } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import StatementQuestion from '@/models/statement/StatementQuestion';
import StatementAnswer from '@/models/statement/StatementAnswer';
import StatementCorrectAnswer from '@/models/statement/StatementCorrectAnswer';
import Image from '@/models/management/Image';
import MultipleChoiceAnswer from '@/components/multiple-choice/MultipleChoiceAnswer.vue';
import CodeFillInAnswerResult from '@/components/code-fill-in/CodeFillInAnswerResult.vue';
import MultipleChoiceAnswerOrderResult from '@/components/multiple-choice/MultipleChoiceAnswerOrderResult.vue';
import MultipleChoiceStatementQuestionDetails from '@/models/statement/questions/MultipleChoiceStatementQuestionDetails';
import CodeOrderAnswerResult from '@/components/code-order/CodeOrderAnswerResult.vue';
//import CombinationItemAnswerResult from '@/components/combination-item/CombinationItemAnswerResult.vue';
@Component({
  components: {
    multiple_choice_order: MultipleChoiceAnswerOrderResult,
    multiple_choice: MultipleChoiceAnswer,
    code_fill_in: CodeFillInAnswerResult,
    code_order: CodeOrderAnswerResult,
    //combination_item: CombinationItemAnswerResult,
  },
})
export default class ResultComponent extends Vue {
  @Model('questionOrder', Number) questionOrder: number | undefined;
  @Prop(StatementQuestion) readonly question!: StatementQuestion;
  @Prop(StatementCorrectAnswer) readonly correctAnswer!: StatementCorrectAnswer;
  @Prop(StatementAnswer) readonly answer!: StatementAnswer;
  @Prop() readonly questionNumber!: number;
  hover: boolean = false;

  @Emit()
  increaseOrder() {
    return 1;
  }

  @Emit()
  decreaseOrder() {
    return 1;
  }

  get questionComponent(){
    return (this.question && 
      this.question.questionDetails.type=='multiple_choice' &&
      (this.question.questionDetails as MultipleChoiceStatementQuestionDetails).ordered)
      ? 'multiple_choice_order'
      : this.question?.questionDetails.type
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>

<style lang="scss" scoped>
.unanswered {
  .question {
    background-color: #761515 !important;
    color: #fff !important;
  }
}

.correct-question {
  .question .question-content {
    background-color: #285f23 !important;
    color: white !important;
  }
  .question .square {
    background-color: #285f23 !important;
    color: white !important;
  }
}

.incorrect-question {
  .question .question-content {
    background-color: #761515 !important;
    color: white !important;
  }
  .question .square {
    background-color: #761515 !important;
    color: white !important;
  }
}
</style>
