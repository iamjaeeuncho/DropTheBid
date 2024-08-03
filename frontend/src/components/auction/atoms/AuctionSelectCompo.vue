<template>
    <v-select
        :items="items"
        v-model="selectedValue"
        @input="$emit('input', $event)"
        outlined
        dense
        required
    >
    <template v-slot:item="{ item }">
      <v-list-item-content>
        <v-list-item-title>{{ item.text }}</v-list-item-title>
      </v-list-item-content>
    </template>
  </v-select>
</template>

<script>
export default {
  props: {
    value: {
      type: [String, Number],
      default:''
    },
    items: {
      type: Array,
      default: () => []
    },
  },
  computed: {
    selectedValue: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit('input', value);
      }
    }
  }
}
</script>

<style>

</style>