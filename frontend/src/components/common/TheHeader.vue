<!-- 헤더 -->
<!-- 작성자: 이주윤 -->

<template>
    <v-app-bar class="pl-10 pr-10" flat fixed>
        <router-link to="/">
            <v-img src="@/assets/team-logo.png" max-height="60" max-width="200" contain class="mr-10"></v-img>
        </router-link>

        <v-select
            v-model="selectedCategory"
            :items="categoryOptions"
            label="Category"
            dense
            hide-details
            solo
            flat
            background-color="rgba(245,245,245,1)"
            style="max-width: 150px"
            @change="onCategorySelect"
        ></v-select>

        <v-spacer></v-spacer>

        <v-text-field
            v-model="searchQuery"
            rounded
            outlined
            dense
            label="검색"
            hide-details="true"
            append-icon="mdi-magnify"
            @click:append="search"
            @keyup.enter="search"
            style="max-width: 300px"
        >
        </v-text-field>

        <v-btn icon class="ml-4" to="/my-page" v-if="isLogIn">
            <v-icon>mdi-account-circle-outline</v-icon>
        </v-btn>

        <v-btn icon class="ml-4" to="/chat" v-if="isLogIn">
            <v-icon>mdi-message-text-outline</v-icon>
        </v-btn>

        <v-btn class="ml-4" to="/signin" v-if="!isLogIn" dark rounded> Login </v-btn>
        <v-btn class="ml-4" to="/" v-if="isLogIn" @click="handleLogout" dark rounded> Logout </v-btn>
        <v-btn class="ml-4" to="/signup" v-if="!isLogIn" dark rounded> Sign in </v-btn>
    </v-app-bar>
</template>

<script>
import axiosInstance from '../../utils/axiosinstance';
import { mapGetters, mapActions } from 'vuex';

const logout = async () => {
    const res = await axiosInstance.put('/members/logout');

    return res.data;
};

export default {
    data() {
        return {
            searchQuery: '',
            categoryOptions: [
                { text: '패션', value: '패션' },
                { text: '가전', value: '가전' },
                { text: '스포츠', value: '스포츠' },
                { text: '수집품', value: '수집품' },
                { text: '기타', value: '기타' },
            ],
        };
    },
    computed: {
        ...mapGetters({
            access_token: 'member/getAccessToken',
            selectedCategory: 'search/getSelectedCategory',
        }),
        isLogIn() {
            return !!this.access_token;
        },
    },

    methods: {
        ...mapActions({
            setCategory: 'search/setCategory',
            clearCategory: 'search/clearCategory',
            newSearch: 'search/newSearch',
        }),

        async onCategorySelect(category) {
            if (!category) return;
            await this.setCategory(category);
            this.$router.push({ path: '/search', query: { category } });
        },

        async search() {
            const trimmedQuery = this.searchQuery.trim();

            if (trimmedQuery) {
                await this.newSearch(trimmedQuery);
            }

            // 현재 경로가 /search이고 쿼리가 같다면 강제로 리로드
            if (this.$route.path === '/search' && this.$route.query.q === trimmedQuery) {
                // 같은 페이지 리로드
                await this.$router.replace({ path: '/search', query: { q: trimmedQuery, _: Date.now() } });
            } else {
                // 다른 페이지로 이동 또는 다른 검색어로 검색
                await this.$router.push({ path: 'search', query: { q: trimmedQuery } });
            }
        },

        handleLogout: function () {
            logout()
                .then(() => {
                    this.$store.dispatch('member/logout');
                    this.clearCategory();

                    if (this.$router.currentRoute.path !== '/') {
                        this.$router.push('/');
                    }
                })
                .catch((error) => {
                    console.log(error);
                });
        },

        clearSearchQuery() {
            this.searchQuery = '';
        },
    },

    watch: {
        $route(to, from) {
            if (to.path !== '/search' && from.path === '/search') {
                this.clearCategory();
                this.clearSearchQuery();
            }
        },
    },

    created() {
        // 컴포넌트가 생성될 때 현재 라우트의 쿼리에서 검색어를 가져와 설정
        const query = this.$route.query.q;
        if (query) {
            this.searchQuery = query;
        }
    },
};
</script>

<style scoped></style>
