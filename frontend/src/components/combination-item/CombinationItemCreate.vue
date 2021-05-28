<template>
  <div class="combination-item-options">


    <v-row
      v-for="(combOption, index) in sQuestionDetails.options"
      :key="combOption.localId"
      data-cy="questionCombOptions"
    >
      <v-col cols="1">
        <div id="index">
            {{  "\n" }}
            {{  index }}
        </div>
      </v-col>
      <v-col cols="6">
        <v-textarea
          v-model="combOption.content"
          :label="`Option ${index}`"
          :data-cy="`Option${index}`"
          rows="1"
          auto-grow
        ></v-textarea>
      </v-col>
      <v-col cols="1">
        <div id="link">
            {{  combOption.link }}
        </div>
      </v-col>
      <v-col cols="1">
        <select v-model="selected" multiple
          :data-cy="`Select${index}`">
          <option>0</option>
          <option>1</option>
          <option>2</option>
          <option>3</option>
          <option>4</option>
          <option>5</option>
          <option>6</option>
          <option>7</option>
          <option>8</option>
          <option>9</option>
        </select>
        <br>
      </v-col>
      <v-col>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              :data-cy="`AddLink${index}`"
              small
              class="ma-1 action-button"
              v-on="on"
              @click="addLink(index, +selected)"
              color="green"
              >add</v-icon
            >
          </template>
          <span>Add Link</span>
        </v-tooltip>
      </v-col>
      <v-col cols="1">
        <v-checkbox
          v-model="combOption.left"
          :label="`Left`"
          :data-cy="`CombOptionLeftofOption${index}`"
          rows="1"
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

  addLink(index : number, link : number){
    if(this.sQuestionDetails.options.length - 1 >= link){
      if(this.sQuestionDetails.options[index].left != this.sQuestionDetails.options[link].left){
        if(!this.sQuestionDetails.options[index].link.includes(link)){
          this.sQuestionDetails.options[index].link.push(link);
          this.sQuestionDetails.options[link].link.push(index);
        }
      }
    }
  }

}
</script>