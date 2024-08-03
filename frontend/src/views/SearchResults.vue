<!-- 검색 결과를 보여주는 페이지 -->
<!-- 작성자: 이주윤 -->

<template>
    <v-container fluid>
        <v-row>
            <!-- 왼쪽 필터링 메뉴 -->
            <v-col cols="3">
                <v-card>
                    <v-card-title>필터</v-card-title>
                    <v-card-text>
                        <v-expansion-panels multiple flat>
                            <v-expansion-panel>
                                <v-expansion-panel-header>카테고리</v-expansion-panel-header>
                                <v-expansion-panel-content>
                                    <v-radio-group v-model="selectedCategory">
                                        <v-radio label="패션" value="패션"></v-radio>
                                        <v-radio label="가전" value="가전"></v-radio>
                                        <v-radio label="스포츠" value="스포츠"></v-radio>
                                        <v-radio label="수집품" value="수집품"></v-radio>
                                    </v-radio-group>
                                </v-expansion-panel-content>
                            </v-expansion-panel>
                            <v-expansion-panel>
                                <v-expansion-panel-header>상태</v-expansion-panel-header>
                                <v-expansion-panel-content>
                                    <v-radio-group v-model="selectedCondition">
                                        <v-radio label="미개봉" value="미개봉"></v-radio>
                                        <v-radio label="새상품" value="새상품"></v-radio>
                                        <v-radio label="중고 상품" value="중고"></v-radio>
                                    </v-radio-group>
                                </v-expansion-panel-content>
                            </v-expansion-panel>
                            <v-expansion-panel>
                                <v-expansion-panel-header>가격</v-expansion-panel-header>
                                <v-expansion-panel-content>
                                    <v-row>
                                        <v-text-field class="mb-2" v-model="minPrice" label="최소 가격" type="number" prefix="₩" hide-details dense outlined></v-text-field>
                                        <v-text-field v-model="maxPrice" label="최대 가격" type="number" prefix="₩" hide-details dense outlined></v-text-field>
                                    </v-row>
                                </v-expansion-panel-content>
                            </v-expansion-panel>
                        </v-expansion-panels>
                    </v-card-text>
                </v-card>
            </v-col>

            <!-- 오른쪽 검색 결과 -->
            <v-col cols="9">
                <h1>검색 결과: {{ $route.query.q }}</h1>
                <v-row align="center">
                    <v-col cols="6">
                        <p class="mb-0 grey--text text--darken-1">총 {{ searchResults.totalCount }}개의 검색 결과</p>
                    </v-col>
                    <v-col cols="6" class="text-right">
                        <v-select
                            v-model="selectedSort"
                            :items="sortOptions"
                            label="정렬"
                            dense
                            outlined
                            hide-details
                            solo
                            flat
                            style="max-width: 200px; margin-left: auto"
                        ></v-select>
                    </v-col>
                </v-row>
                <v-row v-if="searchResults.auctions && searchResults.auctions.length" align="stretch">
                    <v-col v-for="auction in searchResults.auctions" :key="auction.auctionId" cols="12" sm="6" md="4">
                        <v-card height="100%" class="d-flex flex-column">
                            <v-img :src="getImageUrl(auction.url)" height="200px" contain></v-img>
                            <v-card-title>{{ auction.itemName }}</v-card-title>
                            <v-card-text class="flex-grow-1">
                                <p>시작가: {{ auction.startPrice.toLocaleString() }}원</p>
                                <p>최고 입찰가: {{ auction.highestBid.toLocaleString() }}원</p>
                            </v-card-text>
                            <v-card-actions>
                                <v-btn color="primary" text :to="`/auction/${auction.auctionId}`">자세히 보기</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-col>
                </v-row>
                <v-row v-else class="mt-8">
                    <v-col cols="12" class="text-center">
                        <v-icon size="100" color="grey lighten-1">mdi-magnify-off</v-icon>
                        <h2 class="mt-4 grey--text text--darken-1">검색 결과가 없습니다</h2>
                        <p class="mt-2 grey--text">다른 키워드로 검색하거나 필터를 조정해 보세요.</p>
                        <v-btn light class="mt-4" @click="resetSearch">
                            <v-icon left>mdi-refresh</v-icon>
                            필터 초기화
                        </v-btn>
                        <v-btn dark class="mt-4 ml-2" to="/">
                            <v-icon left>mdi-home</v-icon>
                            홈으로 돌아가기
                        </v-btn>
                    </v-col>
                </v-row>
                <v-pagination
                    v-if="searchResults.auctions && searchResults.auctions.length"
                    v-model="currentPage"
                    :length="totalPages"
                    :total-visible="5"
                    @input="changePage"
                    class="mt-4"
                ></v-pagination>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import noImage from '@/assets/no-image.png';

export default {
    name: 'SearchResults',
    data() {
        return {
            currentPage: 1,
            selectedCategory: null,
            selectedCondition: null,
            minPrice: '',
            maxPrice: '',
            selectedSort: 'latest',
            sortOptions: [
                { text: '최신순', value: 'latest' },
                { text: '인기순', value: 'popular' },
                { text: '낮은가격순', value: 'lowPrice' },
                { text: '높은가격순', value: 'highPrice' },
            ],
        };
    },
    computed: {
        ...mapState('search', ['searchResults', 'searchParams']),
        totalPages() {
            return this.searchResults.totalPages ? Number(this.searchResults.totalPages) : 0;
        },
    },
    methods: {
        ...mapActions('search', ['searchAuctions', 'setSearchParams', 'resetFilters']),

        async changePage(page) {
            const newPage = Number(page);

            // 같은 페이지를 클릭한 경우, 아무 작업도 하지 않음
            if (newPage === this.currentPage) {
                return;
            }

            // 페이지 번호 업데이트
            this.currentPage = newPage;

            // 검색 파라미터 업데이트 및 검색 수행
            await this.updateSearchParams({ newPage });
        },

        async updateSearchParams(newParams) {
            const updatedParams = { ...this.searchParams, ...newParams };

            await this.setSearchParams(updatedParams);

            // 라우터 쿼리 업데이트
            const currentQuery = { ...this.$route.query };
            const newQuery = { ...currentQuery, ...newParams };

            if (JSON.stringify(currentQuery) !== JSON.stringify(newQuery)) {
                await this.$router.push({ query: newQuery }).catch((err) => {
                    if (err.name !== 'NavigationDuplicated') {
                        throw err;
                    }
                });
            }

            // 검색 수행
            await this.searchAuctions(updatedParams);
        },

        getImageUrl(url) {
            return url || noImage;
        },

        resetLocalFilters() {
            this.selectedCategory = null;
            this.selectedCondition = null;
            this.minPrice = '';
            this.maxPrice = '';
            this.selectedSort = null;
        },

        async resetSearch() {
            // 로컬 필터 초기화
            this.resetLocalFilters();

            // Vuex 스토러의 검색 파라미터 초기화
            await this.resetFilters();

            // 라우터 쿼리 파라미터 초기화 (검색어는 유지)
            const searchQuery = this.$route.query.q;
            await this.$router.push({
                path: '/search',
                query: searchQuery ? { q: searchQuery } : {},
            });

            // 검색 다시 수행
            await this.searchAuctions();
        },
    },
    watch: {
        $route: {
            handler(to, from) {
                const toPath = to && to.path;
                const fromPath = from && from.path;
                const toQuery = to && to.query;
                const fromQuery = from && from.query;

                if (toPath !== '/search' && fromPath === '/search') {
                    // 검색 결과 페이지에서 다른 페이지로 이동할 때
                    this.resetFilters();
                    this.resetLocalFilters();
                } else if (toPath === '/search' && toQuery && fromQuery && toQuery.q !== fromQuery.q) {
                    // 새로운 검색어로 검색할 때
                    this.resetFilters();
                    this.resetLocalFilters();
                }
            },
            immediate: true,
        },

        '$route.query': {
            handler(newQuery) {
                //라우트 쿼리가 변경될 때만 검색 파라미터 업데이트
                // newQuery가 undefined가 아닌지 확인
                if (newQuery && JSON.stringify(this.searchParams) !== JSON.stringify(newQuery)) {
                    this.setSearchParams(newQuery);
                }
            },
            deep: true,
            immediate: true,
        },
        selectedCategory(newCategory) {
            this.setSearchParams({ ...this.searchParams, category: newCategory, page: 1 });
        },
        selectedCondition(newCondition) {
            this.setSearchParams({ ...this.searchParams, condition: newCondition, page: 1 });
        },
        minPrice(newMinPrice) {
            this.setSearchParams({ ...this.searchParams, minPrice: newMinPrice, page: 1 });
        },
        maxPrice(newMaxPrice) {
            this.setSearchParams({ ...this.searchParams, maxPrice: newMaxPrice, page: 1 });
        },
        selectedSort(newSort) {
            this.setSearchParams({ ...this.searchParams, sort: newSort, page: 1 });
        },

        currentPage(newPage) {
            this.updateSearchParams({ page: newPage });
        },
    },
    created() {
        // 컴포넌트가 생성될 때 현재 라우트의 쿼리로 검색 파라미터 설정
        if (this.$route.query) {
            const params = { ...this.$route.query };
            if (params.page) {
                params.page = Number(params.page);
            }
            this.setSearchParams(params);
            this.currentPage = params.page || 1;
            this.searchAuctions();
        }

        // 컴포넌트가 생성될 때 스토어의 값으로 로컬 상태 초기화
        this.selectedCategory = this.searchParams.category;
        this.selectedCondition = this.searchParams.condition;
        this.minPrice = this.searchParams.minPrice;
        this.maxPrice = this.searchParams.maxPrice;
        this.selectedSort = this.searchParams.sort;
        this.currentPage = this.searchParams.page;
    },
};
</script>
