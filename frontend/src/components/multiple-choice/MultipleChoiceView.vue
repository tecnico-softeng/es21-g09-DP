<template>
  <ul>
    <li v-for="option in questionDetails.options" :key="option.id">
      <span
        v-if="option.correct && questionDetails.ordered"
        v-html="
          convertMarkDown(
            studentAnswered(option.id) + '**[★' + option.order + ']** ' + option.content
          )
        "
        v-bind:class="[option.correct ? 'font-weight-bold' : '']"
      />
      <span
        v-else-if="option.correct && !questionDetails.ordered"
        v-html="
          convertMarkDown(
            studentAnswered(option.id) + '**[★]** ' + option.content
          )
        "
        v-bind:class="[option.correct ? 'font-weight-bold' : '']"
      />
      <span
        v-else
        v-html="convertMarkDown(studentAnswered(option.id) + option.content)"
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Question from '@/models/management/Question';
import Image from '@/models/management/Image';
import MultipleChoiceQuestionDetails from '@/models/management/questions/MultipleChoiceQuestionDetails';
import MultipleChoiceAnswerDetails from '@/models/management/questions/MultipleChoiceAnswerDetails';

@Component
export default class MultipleChoiceView extends Vue {
  @Prop() readonly questionDetails!: MultipleChoiceQuestionDetails;
  @Prop() readonly answerDetails?: MultipleChoiceAnswerDetails;

  studentAnswered(option: number) {
    let answer = this.answerDetails?.answeredOptions.filter(x=>x.optionId==option)[0]
    if(this.answerDetails && typeof answer !== 'undefined'){
      return '**[S'+(answer.order!=null?answer.order:'')+']** ';
    }
    else{
      return '';
    }
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>