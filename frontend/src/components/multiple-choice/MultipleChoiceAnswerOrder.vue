<template>
  <div class="multiple-choice-order-answer">
      <draggable
        class="multiple-choice-order-answer-options"
        tag="ul"
        :list="questionDetails.options"
        :group="{ name: 'answer', pull: 'clone', put: false }"
        :clone="cloneAnswerFromQuestion"
        draggable="li.dragable"
        :sort="false"
      >
        <h4 class="multiple-choice-order-header" slot="header">
          Answer Options:
          <p class="question-warning">
            All options might used or only part of them.
          </p>
        </h4>
        <li
          v-for="(el, index) in questionDetails.options"
          :key="index"
          :class="{
            dragable: !answerDetails.answeredOptions.find((x) => x.optionId == el.optionId),
          }"
        >
          <v-icon class="handle">
            mdi-drag
          </v-icon>

          <span
            class="content"
            v-html="convertMarkDown(el.content)"
          />
        </li>
      </draggable>
      <draggable
        class="multiple-choice-order-answer-response"
        v-model="answerList"
        group="answer"
        draggable="li"
      >
        <h4 class="multiple-choice-order-header" slot="header">Reponse:</h4>
        <li
          v-for="(el, index) in answerDetails.answeredOptions"
          :key="index"
          class="dragable"
        >
          <v-icon class="handle">
            mdi-drag
          </v-icon>
          <span
            class="content"
            v-html="convertMarkDown(questionDetails.options.find((x) => x.optionId == el.optionId).content)"
          />
          <v-btn @click="removeAnswer(index)" icon small>
            <v-icon>mdi-close-circle-outline</v-icon>
          </v-btn>
        </li>
      </draggable>
    </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import draggable from 'vuedraggable';
import MultipleChoiceStatementQuestionDetails from '@/models/statement/questions/MultipleChoiceStatementQuestionDetails';
import MultipleChoiceStatementAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementAnswerDetails';
import OptionStatementAnswerDetails from '@/models/statement/questions/OptionStatementAnswerDetails';
import StatementOption from '@/models/statement/StatementOption';

@Component({
  components: {
    draggable,
  },
})
export default class MultipleChoiceAnswerOrder extends Vue {
  @Prop(MultipleChoiceStatementQuestionDetails)
  readonly questionDetails!: MultipleChoiceStatementQuestionDetails;
  @Prop(MultipleChoiceStatementAnswerDetails)
  answerDetails!: MultipleChoiceStatementAnswerDetails;

  get answerList() {
    return this.answerDetails.answeredOptions;
  }

  set answerList(value) {
    this.answerDetails.answeredOptions = value;
    this.updateAnswer();
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }

  cloneAnswerFromQuestion(element: StatementOption) {
    return new OptionStatementAnswerDetails({
      optionId: element.optionId,
      order: null,
    });
  }

  removeAnswer(index: number) {
    this.answerDetails.answeredOptions.splice(index, 1);
    this.updateAnswer();
  }

  updateAnswer() {
    this.answerDetails.answeredOptions.forEach(
      (element: OptionStatementAnswerDetails, index: number) => {
        element.order = index+1;
      }
    );
    this.$emit('question-answer-update');
  }
}
</script>

<style lang="scss">
.multiple-choice-order-answer {
  background-color: white;
  display: inline-flex;
  justify-content: space-between;
  align-items: stretch;
  width: 100%;
  position: relative;

  & .multiple-choice-order-header {
    padding: 10px;
    margin: 0px;

    .question-warning {
      padding: 0;
      margin: 0;
      font-weight: 100;
      font-size: smaller;
    }
  }

  & li {
    padding: 10px;
    margin: 5px 0;
    border: 2px solid rgb(202, 202, 202);
    display: flex;
    align-items: center;

    &:not(.dragable) {
      opacity: 0.6;
    }

    & > .content {
      flex-grow: 1;
      max-width: 95%;
      font-weight: bold;
    }

    & > .content > p {
      margin-bottom: 0;
    }
  }

  & > *:not(.question-warning) {
    list-style: none;
    text-align: left;
    flex-grow: 1;
    padding-left: 0 !important;
    width: 50%;
    border: dashed 1px gray;
  }

  & > .multiple-choice-order-answer-response {
    background-color: rgb(241, 241, 241);

    & > .content {
      flex-grow: 1;
      max-width: 90%;
    }
  }
  & i{
    padding: 0;
  }
}
</style>
