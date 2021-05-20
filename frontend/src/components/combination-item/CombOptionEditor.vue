<template>
  <div class="combOption">
    <v-icon class="handle">
      mdi-drag
    </v-icon>
    <v-textarea
      v-model="sCombOption.content"
      :label="`CombOption ${index+1}`"
      :data-cy="`CombOption${index+1}`"
      rows="1"
      auto-grow
      class="px-3"
    ></v-textarea>
    <v-checkbox
      v-model="sCombOption.left"
      @change="$emit('update')"
    ></v-checkbox>
    <v-btn v-if="canDelete" @click="$emit('delete')" icon>
        <v-icon>mdi-close-circle-outline</v-icon>
    </v-btn>
  </div>
</template>

<script lang="ts">
import CombOption from '@/models/management/questions/CombOption';
import { Component, PropSync, Vue, Prop } from 'vue-property-decorator';
import BaseCodeEditor from '@/components/BaseCodeEditor.vue';
@Component({
  components: {
    BaseCodeEditor,
  },
})
export default class CombOptionEditor extends Vue {
  @PropSync('combOption', { type: CombOption })
  sCombOption!: CombOption;
  @Prop({ default: false })
  readonly canDelete!: boolean;
  @Prop()
  readonly index!: Number;
}
</script>

<style lang="scss">
.combOption {
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
div.combOption i {
  padding: 0;
}
</style>