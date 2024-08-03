<template>
    <div>
        <label class="radio-button" :class="{ selected: isSelected, error: rules }" @click="select" :style="inputStyles">
            {{ label }}
            <input type="radio" :value="targetValue" v-model="vModel" />
        </label>
    </div>
</template>

<script>
export default {
    props: {
        label: {
            type: String,
            default: 'label',
        },
        targetValue: {
            type: String,
            default: 'target',
        },
        value: {
            type: String,
            default: '',
        },
        width: {
            type: String,
            default: '395px',
        },
        height: {
            type: String,
            default: '50px',
        },
        error: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        isSelected() {
            return this.value === this.targetValue;
        },
        rules() {
            return this.error;
        },
        inputStyles() {
            return {
                width: this.width,
                height: this.height,
            };
        },
        vModel: {
            get() {
                return this.value;
            },
            set(value) {
                this.$emit('input', value);
            },
        },
    },
    methods: {
        select() {
            this.$emit('input', this.targetValue);
        },
    },
};
</script>

<style scoped>
.radio-button {
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 10px;
    border: 1px solid #9e9e9e;
    background-color: white;
    color: #9e9e9e;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
    text-align: center;
}

.radio-button.selected {
    background-color: black;
    color: white;
}

.radio-button.error {
    color: white;
}

.radio-button input {
    display: none;
}
</style>
