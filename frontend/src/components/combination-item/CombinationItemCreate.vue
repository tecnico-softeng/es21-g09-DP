<template>
  <div class="combination-item-options">
    <v-row>
      <v-col cols="1" offset="10"> Link </v-col>
    </v-row>

    <v-row
      v-for="(combOption, index) in sQuestionDetails.options"
      :key="combOption.localId"
      data-cy="questionCombOptions"
    >
      <v-col cols="10">
        <v-textarea
          v-model="combOption.content"
          :label="`Option ${index + 1}`"
          :data-cy="`Option${index + 1}`"
          rows="1"
          auto-grow
        ></v-textarea>
      </v-col>
      <v-col cols="1">
        <v-textarea
          v-model.number="combOption.link"
          :label="`Link`"
          :data-cy="`CombOptionLinkofOption${index + 1}`"
          rows="1"
          auto-grow
        ></v-textarea>
      </v-col>
      <v-col cols="1">
        <v-checkbox
          v-model="combOption.left"
          :label="`Left`"
          :data-cy="`CombOptionLeftofOption${index + 1}`"
          rows="1"
          auto-grow
        ></v-checkbox>
      </v-col>
      <v-col v-if="sQuestionDetails.options.length > 2">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              :data-cy="`Delete${index + 1}`"
              small
              class="ma-1 action-button"
              v-on="on"
              @click="removeCombOption(index)"
              color="red"
              >close</v-icon
            >
          </template>
          <span>Remove Option</span>
        </v-tooltip>
      </v-col>
    </v-row>

    <v-row>
      <v-btn
        class="ma-auto"
        color="blue darken-1"
        @click="addCombOption"
        data-cy="addCombOptionCombination"
        >Add Option</v-btn
      >
    </v-row>

  </div>
</template>

<script lang="ts">
import { Component, Model, PropSync, Vue, Watch } from 'vue-property-decorator';
import CombinationItemQuestionDetails from '@/models/management/questions/CombinationItemQuestionDetails';
import CombinationItemOptionEditor from '@/components/combination-item/CombOptionEditor.vue';
import CombOption from '@/models/management/questions/CombOption';
import draggable from 'vuedraggable';
@Component({
  components: {
    draggable,
    CombinationItemOptionEditor,
  },
})
export default class CombinationItemCreate extends Vue {
  @PropSync('questionDetails', { type: CombinationItemQuestionDetails })
  sQuestionDetails!: CombinationItemQuestionDetails;

  mounted() {
    this.sQuestionDetails.options.forEach(
      (element: CombOption, index: number) => {
        element.localId = this.sQuestionDetails.localId++;
      }
    );
  }

  addCombOption() {
    var newOption = new CombOption()
    newOption.localId = this.sQuestionDetails.localId++;
    this.sQuestionDetails.options.push(newOption);
  }
  removeCombOption(index: number) {
    this.sQuestionDetails.options.splice(index, 1);
  }

}
</script>