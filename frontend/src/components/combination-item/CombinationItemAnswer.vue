<!--
Used on:
  - QuestionComponent.vue
  - ResultComponent.vue
-->
<template>
  <ul data-cy="optionList" class="option-list">
    <li
      v-for="(n, index) in questionDetails.options.length"
      :key="index"
      v-bind:class="['option', index]"
      @click="
        !isReadonly && selectOption(questionDetails.options[index].optionId)
      "
    >
    <span
        class="option-content"
        v-html="convertMarkDown(questionDetails.options[index].link)"
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Emit } from 'vue-property-decorator';
import CombinationItemStatementQuestionDetails from '@/models/statement/questions/CombinationItemStatementQuestionDetails';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import CombinationItemStatementAnswerDetails from '@/models/statement/questions/CombinationItemStatementAnswerDetails';
import CombinationItemStatementCorrectAnswerDetails from '@/models/statement/questions/CombinationItemStatementCorrectAnswerDetails';
import CombOptionStatementAnswerDetails from '@/models/statement/questions/CombOptionStatementAnswerDetails';
import draggable from 'vuedraggable';

@Component
export default class CombinationItemAnswer extends Vue {
  @Prop(CombinationItemStatementQuestionDetails)
  readonly questionDetails!: CombinationItemStatementQuestionDetails;
  @Prop(CombinationItemStatementAnswerDetails)
  answerDetails!: CombinationItemStatementAnswerDetails;
  @Prop(CombinationItemStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: CombinationItemStatementCorrectAnswerDetails;
  get isReadonly() {
    return !!this.correctAnswerDetails;
  }
  /*optionClass(index: number) {
    if (this.isReadonly) {
      if (
        !!this.correctAnswerDetails &&
        this.correctAnswerDetails.correctOptions
              .map(x => x.id)
              .includes(this.questionDetails.options[index].optionId)
      ) {
        if(this.answerDetails.answeredOptions
              .map(x => x.optionId)
              .includes(this.questionDetails.options[index].optionId)){
                return 'correct-answered';
              }
        else{
          return 'correct'
        }
      } else if (
        this.answerDetails.answeredOptions
              .map(x => x.optionId)
              .includes(this.questionDetails.options[index].optionId)
      ) {
        return 'wrong';
      } else {
        return '';
      }
    } else {
      return this.answerDetails.answeredOptions
            .map(x => x.optionId)
            .includes(this.questionDetails.options[index].optionId)
        ? 'selected'
        : '';
    }
  }*/

  @Emit('question-answer-update')
  selectOption(optionId: number) {
    if (this.answerDetails.answeredOptions.map(x => x.optionId).includes(optionId)) {
      this.answerDetails.answeredOptions=this.answerDetails.answeredOptions.filter(x => x.optionId!=optionId);
    } else {
      this.answerDetails.answeredOptions.push(new CombOptionStatementAnswerDetails({
      optionId: optionId,
      link: [],
      }));
    }
  }


  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>

<style lang="scss" scoped>
.unanswered {
  .correct {
    .option-content {
      background-color: #333333;
      color: rgb(255, 255, 255) !important;
    }
    .option-letter {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
}
.correct-answered {
  .option-content {
    background-color: #299455;
    color: rgb(255, 255, 255) !important;
  }
  .option-letter {
    background-color: #299455 !important;
    color: rgb(255, 255, 255) !important;
  }
}
.incorrect-question {
  .wrong {
    .option-content {
      background-color: #cf2323;
      color: rgb(255, 255, 255) !important;
    }
    .option-letter {
      background-color: #cf2323 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
  .correct {
    .option-content {
      background-color: #333333;
      color: rgb(255, 255, 255) !important;
    }
    .option-letter {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
}
</style>