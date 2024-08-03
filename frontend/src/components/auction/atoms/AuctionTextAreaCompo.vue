<template>
    <div>
        <v-textarea outlined :value="value" maxlength="3000" @input="$emit('input', $event)" required> </v-textarea>
        <p class="hint">{{ value.length }}/3000</p>
    </div>
</template>

<script>
export default {
    props: {
        value: {
            default: 0,
            required: true,
        },
    },
};
</script>

<style scoped>
.hint {
    font-size: 0.875rem;
    color: #6c757d;
}
</style>
