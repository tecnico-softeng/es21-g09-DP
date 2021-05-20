<template>
  <div class="open-ended-view">
    <span>Answer:</span>
    <p style="white-space: pre-line;">{{ response }}</p>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator';
import OpenEndedQuestionDetails from '@/models/management/questions/OpenEndedQuestionDetails';

@Component
export default class OpenEndedView extends Vue {
  @Prop() readonly questionDetails!: OpenEndedQuestionDetails;
  response: string = this.questionDetails.answer;
}
</script>
