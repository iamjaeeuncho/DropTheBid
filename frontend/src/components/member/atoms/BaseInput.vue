<template>
    <div>
        <v-text-field
            class="base-text-field"
            v-model="internalValue"
            :label="label"
            :type="type"
            :rules="computedRules"
            :autocomplete="autocomplete"
            :style="inputStyles"
            :disabled="disabled"
            ref="input"
            @input="updateValue"
            outlined
        ></v-text-field>
    </div>
</template>

<script>
export default {
    props: {
        label: {
            type: String,
            default: 'label',
        },
        type: {
            type: String,
            default: 'text',
        },
        autocomplete: {
            type: String,
            default: 'on',
        },
        width: {
            type: String,
            default: '400px',
        },
        rules: {
            type: Array,
            default: () => [],
        },
        value: {
            type: String,
            default: '',
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        errorMessage: {
            type: String,
            default: '',
        },
    },
    data() {
        return {
            internalValue: this.value,
        };
    },
    computed: {
        inputStyles() {
            return {
                width: this.width,
            };
        },
        computedRules() {
            return this.errorMessage ? [...this.rules, () => this.errorMessage] : this.rules;
        },
    },
    methods: {
        updateValue(event) {
            this.$emit('input', event);
        },
        validate() {
            return this.$refs.input.validate();
        },
    },
    watch: {
        value(newVal) {
            this.internalValue = newVal;
        },
    },
};
</script>

<style scoped>
.base-text-field {
    border-radius: 10px;
}
</style>
