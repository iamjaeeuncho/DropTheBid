<!-- 이 달의 경매를 보여주는 컴포넌트 -->
<!-- 작성자: 이주윤 -->

<template>
    <v-card v-if="monthlyAuction.auctionId" class="mt-4 mx-auto" max-width="1000">
        <v-row no-gutters>
            <v-col cols="12" md="6">
                <v-card-title class="text-h5 font-weight-bold"> 이 달의 경매 </v-card-title>
                <v-card-title>
                    {{ monthlyAuction.itemName }}
                </v-card-title>
                <v-list-item>
                    <v-list-item-content>
                        <v-list-item-title>시작 가격</v-list-item-title>
                        <v-list-item-subtitle style="font-weight: bold; color: black">{{ formatPrice(monthlyAuction.startPrice) }} 원</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item>
                    <v-list-item-content>
                        <v-list-item-title>입찰 가격</v-list-item-title>
                        <v-list-item-subtitle style="font-weight: bold; color: black">{{ formatPrice(monthlyAuction.highestBid) }} 원</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
                <v-row class="my-4 ml-1">
                    <v-col v-for="(time, index) in remainingTime" :key="index" cols="auto">
                        <v-card outlined class="text-center pa-2" min-width="70">
                            <div class="text-h5">{{ time.value }}</div>
                            <div class="caption">{{ time.label }}</div>
                        </v-card>
                    </v-col>
                </v-row>
                <v-card-actions>
                    <v-btn class="ml-2" color="black" rounded dark :to="`/auction/${monthlyAuction.auctionId}`" style="width: 50%"> 제품 보러가기 → </v-btn>
                </v-card-actions>
            </v-col>
            <v-col cols="12" md="6" class="d-flex align-center justify-center">
                <v-img :src="getImageUrl(monthlyAuction.url)" alt="Auction Item Image" class="rounded-lg" contain max-height="400" max-width="400"></v-img>
            </v-col>
        </v-row>
    </v-card>
    <v-card v-else class="mt-4 mx-auto text-center pa-5" max-width="1000">
        <v-card-text>
            <p class="text-h6">현재 이 달의 경매 상품이 없습니다.</p>
        </v-card-text>
    </v-card>
</template>

<script>
import noImage from '@/assets/no-image.png';

export default {
    name: 'AuctionOfTheMonth',
    data() {
        return {
            monthlyAuction: {},
            remainingTime: [
                { value: 0, label: 'Days' },
                { value: 0, label: 'Hours' },
                { value: 0, label: 'Mins' },
                { value: 0, label: 'Secs' },
            ],
            timer: null,
        };
    },

    mounted() {
        this.getMonthlyAuction();
    },

    methods: {
        getMonthlyAuction() {
            this.$axios
                .get('/auctions/month')
                .then((res) => {
                    this.monthlyAuction = res.data.auction || {};
                    this.startTimer();
                })
                .catch((err) => {
                    console.error('getMonthlyAuction() 실행 중 에러 발생:', err);
                    this.monthlyAuction = {};
                });
        },
        formatPrice(price) {
            return price ? price.toLocaleString('ko-KR') : '0';
        },
        startTimer() {
            this.updateRemainingTime();
            this.timer = setInterval(this.updateRemainingTime, 1000);
        },
        updateRemainingTime() {
            const now = new Date();
            const dueDate = new Date(this.monthlyAuction.dueDate);
            const diff = dueDate - now;

            if (diff <= 0) {
                clearInterval(this.timer);
                this.remainingTime = this.remainingTime.map((time) => ({ ...time, value: 0 }));
                return;
            }

            const days = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((diff % (1000 * 60)) / 1000);

            this.remainingTime = [
                { value: days, label: 'Days' },
                { value: hours, label: 'Hours' },
                { value: minutes, label: 'Mins' },
                { value: seconds, label: 'Secs' },
            ];
        },

        getImageUrl(url) {
            return url || noImage;
        },
    },

    beforeDestroy() {
        if (this.timer) {
            clearInterval(this.timer);
        }
    },
};
</script>

<style scoped></style>
