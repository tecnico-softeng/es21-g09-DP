<template>
  <ul>
    <h3> Options </h3>
    <li v-for="option in questionDetails.options" :key="option.id">
      <span
        v-if="option.left"
        v-html="
          convertMarkDown('Option  ID:' + option.id + '| Link:' + option.link + '| Content:' + option.content)
        "
      />
      <span
        v-else
        v-html="
          convertMarkDown('Answer  ID:' + option.id + '| Link:' + option.link + '| Content:' + option.content)
        "
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Question from '@/models/management/Question';
import Image from '@/models/management/Image';
import CombinationItemQuestionDetails from '@/models/management/questions/CombinationItemQuestionDetails';

@Component
export default class CombinationItemView extends Vue {
  @Prop() readonly questionDetails!: CombinationItemQuestionDetails;

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
