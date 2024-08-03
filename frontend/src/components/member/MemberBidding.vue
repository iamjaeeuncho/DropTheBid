<template>
    <div class="auction-container">
        <v-row>
            <v-col v-for="item in auctionItems" :key="item.auctionId" cols="12" sm="6" md="4">
                <v-card>
                    <base-image :src="item.url" height="200px" objectFit="cover"></base-image>

                    <div class="auction-content">
                        <base-span
                            :spanContent="item.auctionStatusName"
                            :backgroundColor="getColor(item.auctionStatusId).backgroundColor"
                            :color="getColor(item.auctionStatusId).color"
                            fontSize="10px"
                            borderRadius="5px"
                            padding="5px"
                        ></base-span>
                        <base-span :spanContent="item.itemName" fontSize="19px"></base-span>
                    </div>
                    <div class="auction-price">
                        <base-span spanContent="시작 금액" fontSize="16px"></base-span>
                        <base-span :spanContent="item.startPrice" fontSize="16px"></base-span>
                    </div>
                    <div class="auction-price">
                        <base-span spanContent="최종 금액" fontSize="16px"></base-span>
                        <base-span :spanContent="item.maxPrice" fontSize="16px"></base-span>
                    </div>
                    <div class="auction-button">
                        <base-button
                            v-if="item.auctionStatusId === 3"
                            buttonName="결제"
                            type="button"
                            @click="goToPayment(item.auctionId)"
                            width="145px"
                            height="30px"
                        ></base-button>
                        <base-button
                            v-if="item.auctionStatusId === 3"
                            buttonName="취소"
                            type="button"
                            backgroundColor="#000"
                            color="#fff"
                            width="145px"
                            height="30px"
                            @click="openCancelModal(item.auctionId)"
                        ></base-button>
                    </div>
                </v-card>
            </v-col>
        </v-row>
        <v-pagination v-if="auctionItems && auctionItems.length" v-model="currentPage" :length="totalPages" :total-visible="5" @input="changePage" class="mt-4"></v-pagination>

        <v-dialog v-model="cancelModal" max-width="500px">
            <v-card>
                <v-card-title class="headline">낙찰 취소</v-card-title>
                <v-card-text>
                    <span class="di-content">정말로 낙찰을 취소하시겠습니까?</span>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <div class="di-button">
                        <base-button base-button buttonName="예" type="button" @click="confirmCancel" width="100px" height="30px"></base-button>
                    </div>
                    <div class="di-button">
                        <base-button buttonName="아니오" type="button" backgroundColor="#000" color="#fff" width="100px" height="30px" @click="cancelModal = false"></base-button>
                    </div>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
import axiosInstance from '../../utils/axiosinstance.js';

import BaseImage from './atoms/BaseImage.vue';
import BaseSpan from './atoms/BaseSpan.vue';
import BaseButton from './atoms/BaseButton.vue';

const loadMemberAuctionBidding = async (page) => {
    const res = await axiosInstance.get('/members/info/auctions/bidding', {
        params: { page },
    });

    return res.data;
};

const deleteAuctionBidding = async (auctionId) => {
    console.log(auctionId);
    const res = await axiosInstance.delete(`/auctions/${auctionId}`);

    return res.data;
};

export default {
    components: { BaseImage, BaseSpan, BaseButton },
    data() {
        return {
            auctionItems: [],
            currentPage: 1,
            totalPages: 1,
            cancelModal: false,
            selectedAuctionId: null,
        };
    },
    created() {
        this.loadInfo();
    },
    methods: {
        async loadInfo() {
            try {
                const memberAuctionBiddings = await loadMemberAuctionBidding(this.currentPage);
                this.auctionItems = memberAuctionBiddings.auctions;
                this.totalPages = memberAuctionBiddings.totalPages;
            } catch (error) {
                console.error(error);
            }
        },
        async changePage(page) {
            this.currentPage = page;
            await this.loadInfo();
        },
        goToPayment(auctionId) {
            this.$router.push(`/auctions/${auctionId}/payment`);
        },
        openCancelModal(auctionId) {
            this.selectedAuctionId = auctionId;
            this.cancelModal = true;
        },
        async confirmCancel() {
            this.cancelModal = false;

            try {
                await deleteAuctionBidding(this.selectedAuctionId);
                await this.loadInfo();
            } catch (error) {
                console.error(error);
            }
        },
        getColor(status) {
            switch (status) {
                case 1:
                    return {
                        backgroundColor: '#eeeeee',
                        color: '#000000',
                    };
                case 2:
                    return {
                        backgroundColor: '#ebfaeb',
                        color: '#3cd139',
                    };
                case 3:
                    return {
                        backgroundColor: '#fad7e8',
                        color: '#d90f79',
                    };
                default:
                    return {
                        backgroundColor: '#ffffff',
                        color: '#000000',
                    };
            }
        },
    },
};
</script>

<style scoped>
.auction-container {
    width: 1000px;
}
.auction-content {
    padding: 0px 12px;
}
.auction-price {
    display: flex;
    justify-content: space-between;
    padding: 0px 12px;
}
.auction-button {
    display: flex;
    justify-content: space-between;
    padding: 0px 12px;
    margin-top: 10px;
}
.di-content {
    font-weight: bold;
    font-size: 18px;
}
.di-button {
    padding: 2px;
}
</style>
