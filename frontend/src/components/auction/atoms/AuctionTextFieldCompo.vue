<template>
    <v-text-field :value="value" @input="$emit('input', $event)" single-line outlined dense :type="type" :min="min" class="auction-text-field" required></v-text-field>
</template>

<script>
export default {
    props: {
        value: {
            required: true,
        },
        type: {
            type: String,
            default: 'text',
        },
        min: {
            type: [Number, String],
            default: null,
        },
    },
};
</script>

<style scoped>
.auction-text-field {
    width: 100%; /* 텍스트 필드가 부모 요소의 전체 너비를 차지하도록 설정 */
}
</style>
