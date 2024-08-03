<!-- 최근 등록된 경매 리스트를 보여주는 컴포넌트 -->
<!-- 작성자: 이주윤 -->

<template>
    <div class="carousel-container">
        <v-divider class="mt-6 mb-2 mx-auto" style="width: 1000px"></v-divider>
        <h2 style="text-align: center">새로 등록된 경매</h2>
        <v-carousel v-if="newAuctions.length > 0" hide-delimiters show-arrows-on-hover height="auto" style="width: 1000px; margin: 0 auto">
            <v-carousel-item v-for="(group, index) in auctionGroups" :key="index">
                <v-container>
                    <v-row>
                        <v-col v-for="item in group" :key="item.auctionId" cols="12" sm="6" md="3">
                            <v-card height="400px" class="d-flex flex-column" @click="navigateToAuction(item.auctionId)" :class="{ 'clickable-card': true }">
                                <v-img :src="getImageUrl(item.url)" height="200px" contain></v-img>
                                <v-card-title class="title-container pa-2">
                                    <div class="title-content" :style="{ fontSize: calculateFontSize(item.itemName) }">
                                        {{ item.itemName }}
                                    </div>
                                </v-card-title>
                                <v-card-text class="flex-grow-1 d-flex flex-column justify-space-between">
                                    <div>
                                        <div class="d-flex justify-space-between mb-1" style="color: black">
                                            <span class="body-2">시작 가격:</span>
                                            <span class="body-2 font-weight-bold">{{ formatPrice(item.startPrice) }} 원</span>
                                        </div>
                                        <div class="d-flex justify-space-between" style="color: black">
                                            <span class="body-2">입찰 가격:</span>
                                            <span class="body-2 font-weight-bold">{{ formatPrice(item.highestBid) }} 원</span>
                                        </div>
                                    </div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </v-carousel-item>
        </v-carousel>
        <v-card v-else class="text-center pa-5" style="width: 1000px; margin: 0 auto">
            <v-card-text>
                <p class="text-h6">현재 등록된 경매가 없습니다.</p>
            </v-card-text>
        </v-card>
    </div>
</template>

<script>
import noImage from '@/assets/no-image.png';

export default {
    name: 'NewAuctions',
    data() {
        return {
            newAuctions: [],
        };
    },

    mounted() {
        this.getNewAuctions();
    },

    computed: {
        auctionGroups() {
            const groups = [];
            for (let i = 0; i < this.newAuctions.length; i += 4) {
                groups.push(this.newAuctions.slice(i, i + 4));
            }
            return groups;
        },
    },

    methods: {
        getNewAuctions() {
            this.$axios
                .get('/auctions/new')
                .then((res) => {
                    this.newAuctions = res.data.auctions || [];
                })
                .catch((err) => {
                    console.err('getNewAuctions() 실행 중 에러 발생: ', err);
                    this.newAuctions = [];
                });
        },

        formatPrice(price) {
            return price.toLocaleString('ko-KR');
        },

        calculateFontSize(title) {
            const baseSize = 20; // 기본 폰트 크기
            const minSize = 12; // 최소 폰트 크기
            const maxLength = 10; // 이 길이 이상이면 폰트 크기를 줄임

            if (title.length <= maxLength) {
                return `${baseSize}px`;
            } else {
                const newSize = Math.max(baseSize - (title.length - maxLength), minSize);
                return `${newSize}px`;
            }
        },

        navigateToAuction(auctionId) {
            this.$router.push(`/auction/${auctionId}`);
        },

        getImageUrl(url) {
            return url || noImage;
        },
    },
};
</script>

<style scoped>
.title-container {
    height: 80px; /* 제목 컨테이너의 고정 높이 */
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
}
.clickable-card {
    cursor: pointer;
    transition: transform 0.2s;
}

.clickable-card:hover {
    transform: scale(1.03);
}
</style>
