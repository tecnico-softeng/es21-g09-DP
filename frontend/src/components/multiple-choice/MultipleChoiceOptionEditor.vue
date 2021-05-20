<template>
  <div class="option">
    <v-icon class="handle">
      mdi-drag
    </v-icon>
    <div v-if="isOrdered" class="text-center">
      {{ sOption.order == null ? `-` : sOption.order }}
    </div>
    <v-textarea
      v-model="sOption.content"
      :label="`Option ${index+1}`"
      :data-cy="`Option${index+1}`"
      rows="1"
      auto-grow
      class="px-3"
    ></v-textarea>
    <v-checkbox
      :data-cy="`Checkbox${index+1}`"
      v-model="sOption.correct"
      @change="$emit('update')"
    ></v-checkbox>
    <v-btn
      v-if="canDelete"
      @click="$emit('delete')"
      :data-cy="`Delete${index+1}`"
      icon>
        <v-icon>mdi-close-circle-outline</v-icon>
    </v-btn>
  </div>
</template>

<script lang="ts">
import Option from '@/models/management/Option';
import { Component, PropSync, Vue, Prop } from 'vue-property-decorator';
import BaseCodeEditor from '@/components/BaseCodeEditor.vue';

@Component({
  components: {
    BaseCodeEditor,
  },
})
export default class MultipleChoiceOptionEditor extends Vue {
  @PropSync('option', { type: Option })
  sOption!: Option;
  @PropSync('ordered')
  readonly isOrdered!: boolean;
  @Prop({ default: false })
  readonly canDelete!: boolean;
  @Prop()
  readonly index!: Number;
}
</script>

<style lang="scss">
.option {
  background-color: white;
  display: inline-flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin: 10px 0px;

  & > * {
    min-width: 20px;
    margin: auto;
  }
}


div.option i {
  padding: 0;
}
</style>
