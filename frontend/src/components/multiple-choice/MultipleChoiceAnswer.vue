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
      v-bind:class="['option', optionClass(index)]"
      @click="
        !isReadonly && selectOption(questionDetails.options[index].optionId)
      "
    >
      <span
        v-if="
          isReadonly &&
          optionClass(index).includes('correct')
        "
        class="fas fa-check option-letter"
      />
      <span
        v-else-if="
          isReadonly &&
          optionClass(index)=='wrong'
        "
        class="fas fa-times option-letter"
      />
      <span v-else class="option-letter">{{
        String.fromCharCode(65 + index)
      }}</span>
      <span
        class="option-content"
        v-html="convertMarkDown(questionDetails.options[index].content)"
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Emit } from 'vue-property-decorator';
import MultipleChoiceStatementQuestionDetails from '@/models/statement/questions/MultipleChoiceStatementQuestionDetails';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import MultipleChoiceStatementAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementAnswerDetails';
import MultipleChoiceStatementCorrectAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementCorrectAnswerDetails';
import OptionStatementAnswerDetails from '@/models/statement/questions/OptionStatementAnswerDetails';
import draggable from 'vuedraggable';

@Component({
  components: {
    draggable,
  },
})
export default class MultipleChoiceAnswer extends Vue {
  @Prop(MultipleChoiceStatementQuestionDetails)
  readonly questionDetails!: MultipleChoiceStatementQuestionDetails;
  @Prop(MultipleChoiceStatementAnswerDetails)
  answerDetails!: MultipleChoiceStatementAnswerDetails;
  @Prop(MultipleChoiceStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: MultipleChoiceStatementCorrectAnswerDetails;

  get isReadonly() {
    return !!this.correctAnswerDetails;
  }

  optionClass(index: number) {
    if (this.isReadonly) {
      if (
        !!this.correctAnswerDetails &&
        this.correctAnswerDetails.correctOptions
              .filter(x => x.correct)
              .map(x => x.optionId)
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
  }

  @Emit('question-answer-update')
  selectOption(optionId: number) {
    if (this.answerDetails.answeredOptions.map(x => x.optionId).includes(optionId)) {
      this.answerDetails.answeredOptions=this.answerDetails.answeredOptions.filter(x => x.optionId!=optionId);
    } else {
      this.answerDetails.answeredOptions.push(new OptionStatementAnswerDetails({
      optionId: optionId,
      order: null,
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
