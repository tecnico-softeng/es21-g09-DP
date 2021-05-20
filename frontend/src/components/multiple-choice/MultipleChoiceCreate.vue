<template>
  <div class="multiple-choice-options">
    <v-row align="center">
          <v-switch
          color="primary"
          :label="`Ordered`"
          :messages="sQuestionDetails.ordered ? `Please place the correct options by order`:null"
          v-model="sQuestionDetails.ordered"
          @change="updateOrder"
          data-cy="Ordered"
          ></v-switch>
          <v-spacer></v-spacer>
          <v-btn 
            color="primary" 
            data-cy="addOptionMultipleChoice" 
            @click="addOption" 
            >New Option</v-btn
          >
    </v-row>
    <draggable
      tag="div"
      :list="sQuestionDetails.options"
      class="list-group"
      handle=".handle"
      @end="updateOrder()"
    >
      <div
        v-for="(element, index) in sQuestionDetails.options"
        :key="element.localId"
        data-cy="questionOptionsInput"
      >
        <MultipleChoiceOptionEditor
          :option.sync="sQuestionDetails.options[index]"
          :canDelete="sQuestionDetails.options.length > 2"
          :index="index"
          :ordered.sync="sQuestionDetails.ordered"
          @delete="removeOption(index)"
          @update="updateOrder()"
        />
      </div>
    </draggable>
  </div>
</template>

<script lang="ts">
import { Component, Model, PropSync, Vue, Watch } from 'vue-property-decorator';
import MultipleChoiceQuestionDetails from '@/models/management/questions/MultipleChoiceQuestionDetails';
import MultipleChoiceOptionEditor from '@/components/multiple-choice/MultipleChoiceOptionEditor.vue';
import Option from '@/models/management/Option';
import draggable from 'vuedraggable';


@Component({
  components: {
    draggable,
    MultipleChoiceOptionEditor,
  },
})
export default class MultipleChoiceCreate extends Vue {
  @PropSync('questionDetails', { type: MultipleChoiceQuestionDetails })
  sQuestionDetails!: MultipleChoiceQuestionDetails;

  mounted() {
    this.sQuestionDetails.options.forEach(
      (element: Option, index: number) => {
        element.localId = this.sQuestionDetails.localId++;
      }
    );
  }

  addOption() {
    var newOption = new Option()
    newOption.localId = this.sQuestionDetails.localId++;
    this.sQuestionDetails.options.push(newOption);
    this.updateOrder()
  }

  removeOption(index: number) {
    this.sQuestionDetails.options.splice(index, 1);
    this.updateOrder()
  }

  updateOrder(){
    if(this.sQuestionDetails.ordered){
      var i = 1;
      this.sQuestionDetails.options.forEach(
        (element: Option, index: number) => {
          element.order = element.correct ? i++ : null;
        }
      );

    }else{
      this.sQuestionDetails.options.forEach(
        (element: Option, index: number) => {
          element.order = null;
        }
      );
    }
    this.sQuestionDetails.options = this.sQuestionDetails.options.sort(
      (a, b) => {
        if (a.order == null) {
          return 1;
        }
        if (b.order == null) {
          return -1;
        }
        return a.order > b.order ? 1 : -1;
      }
    );
  }
}
</script>
