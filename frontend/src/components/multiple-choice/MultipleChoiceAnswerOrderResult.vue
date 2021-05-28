<template>
  <div class="code-order-answer-result">
    <div class="code-order-answer-student">
      <h4 class="code-order-header">Answer:</h4>
      <div
        v-for="(el, index) in answerDetails.answeredOptions"
        :key="index"
        :class="{ correct: isCorrect(el, index) }"
      >
        <span class="order" v-html="el.order+': '" />
        <span
            class="content"
            v-html="convertMarkDown(optionById(el.optionId).content)"
          />
        <span
          class="is-correct"
          v-html="isCorrect(el, index) ? ' ✔ ' : ' ✖ '"
        />
      </div>
    </div>
    <div class="code-order-answer-correct">
      <h4 class="code-order-header">Correct Order:</h4>
      <div
        v-for="(el, index) in correctAnswerDetails.correctOptions"
        :key="index"
        :class="{ 'not-used': el.order == null }"
      >
        <span
            class="content"
            v-html="convertMarkDown(optionById(el.optionId).content)"
          />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import BaseCodeEditor from '@/components/BaseCodeEditor.vue';
import OptionStatementAnswerDetails from '@/models/statement/questions/OptionStatementAnswerDetails';
import MultipleChoiceStatementQuestionDetails from '@/models/statement/questions/MultipleChoiceStatementQuestionDetails';
import MultipleChoiceStatementAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementAnswerDetails';
import MultipleChoiceStatementCorrectAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementCorrectAnswerDetails';

@Component({
  components: {
    BaseCodeEditor,
  },
})
export default class MultipleChoiceAnswerOrderResult extends Vue {
  @Prop(MultipleChoiceStatementQuestionDetails)
  readonly questionDetails!: MultipleChoiceStatementQuestionDetails;
  @Prop(MultipleChoiceStatementAnswerDetails)
  readonly answerDetails!: MultipleChoiceStatementAnswerDetails;
  @Prop(MultipleChoiceStatementCorrectAnswerDetails)
  readonly correctAnswerDetails!: MultipleChoiceStatementCorrectAnswerDetails;

  optionById(optionId: number) {
    return this.questionDetails.options.find((x) => x.optionId == optionId);
  }

  isCorrect(element: OptionStatementAnswerDetails, index: number) {
    let correctPlaced = this.correctAnswerDetails.correctOptions[index];
    return (
      element.optionId == correctPlaced.optionId && correctPlaced.order != null
    );
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>

<style lang="scss">
.code-order-answer-result {
  background-color: white;
  display: inline-flex;
  justify-content: space-between;
  align-items: stretch;
  width: 100%;
  position: relative;

  & > div {
    max-width: 50%;
  }

  & .content {
    text-align: left;
  }

  & .CodeMirror {
    height: auto;
  }

  & .code-order-header {
    padding: 10px;
    margin: 0px 10px;
  }

  & > div {
    width: 100%;
  }

  & > div > div {
    padding: 10px;
    margin: 5px 0;
    border: 2px solid rgb(202, 202, 202);
  }

  & > .code-order-answer-student > div {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    & .content {
      flex-grow: 1;
      max-width: 95%;
      font-weight: bold;
    }
    &.correct {
      background-color: #299455;
      color: white;
    }
    &:not(.correct) {
      background-color: #cf2323;
      color: white;
    }
  }

  & .not-used {
    opacity: 0.6;
  }

  & .content p {
    margin-bottom: 0;
  }
}
</style>
