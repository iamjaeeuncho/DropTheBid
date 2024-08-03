<template>
    <div>
        <span :style="styles"> {{ spanContent }}</span>
    </div>
</template>

<script>
export default {
    props: {
        spanContent: {
            type: [String, Number],
            required: true,
        },
        backgroundColor: {
            type: String,
            default: '#fff',
        },
        color: {
            type: String,
            default: '#000',
        },
        fontSize: {
            type: String,
            default: '10px',
        },
        borderRadius: {
            type: String,
            default: '0px',
        },
        padding: {
            type: String,
            default: '0px',
        },
    },
    computed: {
        styles() {
            return {
                backgroundColor: this.backgroundColor,
                color: this.color,
                fontSize: this.fontSize,
                borderRadius: this.borderRadius,
                padding: this.padding,
            };
        },
    },
};
</script>
<style scoped></style>
