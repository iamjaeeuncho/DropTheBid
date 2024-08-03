<template>
    <div class="container">
        <v-img :src="src" :style="inputStyles" />
    </div>
</template>

<script>
export default {
    props: {
        src: {
            type: String,
            default: '',
        },
        width: {
            type: String,
            default: 'auto',
        },
        height: {
            type: String,
            default: 'auto',
        },
        objectFit: {
            type: String,
            default: 'none',
            validator: (value) => ['fill', 'contain', 'cover', 'none', 'scale-down'].includes(value),
        },
    },
    computed: {
        inputStyles() {
            return {
                width: this.width,
                height: this.height,
                objectFit: this.objectFit,
            };
        },
    },
};
</script>

<style scoped>
.container {
    border-radius: 10px;
    margin-top: 50px;
}
</style>
