<template>
  <div class="open-ended-create">
    <v-textarea v-model="sQuestionDetails.answer" placeholder="Write Answer" label="Answer" data-cy="answerTextArea" auto-grow></v-textarea>
  </div>
</template>

<script lang="ts">
import { Component, Vue, PropSync } from 'vue-property-decorator';
import OpenEndedQuestionDetails from '@/models/management/questions/OpenEndedQuestionDetails';

@Component
export default class OpenEndedQuestionEdit extends Vue {
  @PropSync('questionDetails', { type: OpenEndedQuestionDetails })
  sQuestionDetails!: OpenEndedQuestionDetails;
}
</script>
