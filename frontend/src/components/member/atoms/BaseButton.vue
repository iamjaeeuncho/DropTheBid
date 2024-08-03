<template>
    <div>
        <v-btn class="base-button" :type="type" :style="buttonStyles" @click="handleClick">
            {{ buttonName }}
        </v-btn>
    </div>
</template>

<script>
export default {
    props: {
        buttonName: {
            type: String,
            default: '',
        },
        type: {
            type: String,
            default: 'button',
        },
        backgroundColor: {
            type: String,
            default: '#fff',
        },
        color: {
            type: String,
            default: '#000',
        },
        borderColor: {
            type: String,
            default: '#000',
        },
        width: {
            type: String,
            default: '400px',
        },
        height: {
            type: String,
            default: '50px',
        },
        to: {
            type: String,
            default: null,
        },
    },
    computed: {
        buttonStyles() {
            return {
                backgroundColor: this.backgroundColor,
                color: this.color,
                borderColor: this.borderColor,
                width: this.width,
                height: this.height,
            };
        },
    },
    methods: {
        handleClick() {
            if (this.to) {
                this.$router.push(this.to);
            } else {
                this.$emit('click');
            }
        },
    },
};
</script>

<style scoped>
.base-button {
    border-radius: 25px;
    margin-bottom: 10px;
    border: 1px solid #000;
}
</style>
