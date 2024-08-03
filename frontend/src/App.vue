<template>
    <v-app id="app">
        <the-header />
        <v-main class="mt-16">
            <the-content />
        </v-main>
        <the-footer />
    </v-app>
</template>

<script>
import TheHeader from '@/components/common/TheHeader.vue';
import TheFooter from '@/components/common/TheFooter.vue';
import TheContent from '@/components/common/TheContent.vue';

export default {
    name: 'App',

    components: {
        TheHeader,
        TheContent,
        TheFooter,
    },
    data: () => ({
        //
    }),
};
</script>

<style>
@import '~@/assets/style.css';
</style>
