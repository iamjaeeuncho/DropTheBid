<template>
  <div>
    <input
      type="file"
      ref="fileInput"
      style="display: none;"
      @change="handleFileChange"
      :multiple="multiple"
      :accept="accept"
    />
    <v-btn @click="triggerFileInput">
      {{ buttonText }}
    </v-btn>
    <div v-if="hint" class="hint">{{ hint }}</div>
  </div>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
      default: ''
    },
    multiple: {
      type: Boolean,
      default: false
    },
    accept: {
      type: String,
      default: '*/*'
    },
    buttonText: {
      type: String,
      default: '파일 선택'
    },
    hint: {
      type: String,
      default: ''
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange() {
      const files = this.$refs.fileInput.files;
      this.$emit('files-selected', files);
    }
  }
}
</script>

<style scoped>
.hint {
  margin-top: 8px;
  font-size: 0.875rem;
  color: #6c757d;
}

.v-btn {
  margin-top: 8px;
}
</style>
