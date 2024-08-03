<template>
    <div class="title-container">
        <h1>{{ title }}</h1>
    </div>
</template>

<script>
export default {
    props: {
        title: {
            type: String,
            default: 'Title',
        },
    },
};
</script>

<style scoped>
.title-container {
    margin: 50px 0px 30px 0px;
}
</style>
