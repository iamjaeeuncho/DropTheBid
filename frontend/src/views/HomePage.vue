<!-- 홈페이지 -->
<!-- 작성자: 이주윤 -->

<template>
    <div class="home">
        <auction-of-the-month></auction-of-the-month>
        <new-auctions></new-auctions>
        <hot-auctions></hot-auctions>
        <v-btn
            class="auction-btn"
            fab
            dark
            x-large
            color="black"
            @click="goToRegisterAuctionPage"
        >
            <v-icon dark>
                mdi-plus
            </v-icon>
        </v-btn>
    </div>
</template>

<script>
import AuctionOfTheMonth from '@/components/home/AuctionOfTheMonth.vue';
import NewAuctions from '@/components/home/NewAuctions.vue';
import HotAuctions from '@/components/home/HotAuctions.vue';

export default {
    name: 'HomePage',
    components: {
        AuctionOfTheMonth,
        NewAuctions,
        HotAuctions,
    },
    methods: {
    goToRegisterAuctionPage() {
      this.$router.push('/auction');
    }
  }
};
</script>

<style scoped>
    .auction-btn {
    position: fixed;
    bottom: 50px; 
    right: 50px;
    z-index: 1000;
    }
</style>
