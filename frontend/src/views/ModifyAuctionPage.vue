<template>
    <div class="container">
        <base-title title="경매 상품 수정"></base-title>
        <auction-form-compo v-if="isDataLoaded" :initialAuctionData="auctionData" :initialAuctionImages="images" :initialMainImageIndex="mainImageIndex" :isEditMode="true">
        </auction-form-compo>
        <div v-else>
            <p>Loading...</p>
        </div>
    </div>
</template>

<script>
import AuctionFormCompo from '@/components/auction/AuctionFormCompo.vue';
import BaseTitle from '@/components/member/atoms/BaseTitle.vue';
import axios from 'axios';

export default {
    components: {
        AuctionFormCompo,
        BaseTitle,
    },
    data() {
        return {
            auctionData: {
                auctionId: this.$route.params.id,
                itemName: '',
                categoryId: null,
                auctionProductStatusId: null,
                location: '',
                startPrice: 0,
                startDate: '',
                dueDate: '',
                description: '',
            },
            images: [],
            mainImageIndex: null,
            isDataLoaded: false,
        };
    },
    methods: {
        async setAuctionData() {
            try {
                const res = await axios.get(`/auctions/all/${this.$route.params.id}`, {
                    headers: {
                        'Content-Type': 'application/json',
                        withCredentials: true,
                    },
                });

                if (res.status === 200) {
                    console.log(res.data);
                    this.auctionData = res.data.auctionData;
                    this.images = res.data.images;
                    this.mainImageIndex = res.data.mainImageIndex;
                    this.isDataLoaded = true; // 데이터 로드 완료
                } else {
                    console.log(res.statusText);
                }
            } catch (error) {
                console.error('Error:', error);
            }
        },
    },
    mounted() {
        this.setAuctionData();
    },
};
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 1200px; /* 컨테이너의 너비를 1200px로 설정 */
    margin: 0 auto; /* 가로 방향 가운데 정렬 */
}
</style>
