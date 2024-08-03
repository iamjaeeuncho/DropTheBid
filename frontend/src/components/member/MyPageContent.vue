<template>
    <div class="container">
        <base-title title="마이페이지"></base-title>

        <div class="radio-group">
            <base-radio-button label="내 정보 수정하기" targetValue="MemberInfo" v-model="page" width="330px"></base-radio-button>
            <base-radio-button label="나의 경매 물품" targetValue="MemberAuctionItem" v-model="page" width="330px"></base-radio-button>
            <base-radio-button label="나의 입찰 내역" targetValue="MemberBidding" v-model="page" width="330px"></base-radio-button>
        </div>

        <div v-if="page === 'MemberInfo'">
            <member-info></member-info>
        </div>
        <div v-if="page === 'MemberAuctionItem'">
            <member-auction-item></member-auction-item>
        </div>
        <div v-if="page === 'MemberBidding'">
            <member-bidding></member-bidding>
        </div>
    </div>
</template>

<script>
import BaseRadioButton from './atoms/BaseRadioButton.vue';
import BaseTitle from './atoms/BaseTitle.vue';
import MemberInfo from './MemberInfo.vue';
import MemberAuctionItem from './MemberAuctionItem.vue';
import MemberBidding from './MemberBidding.vue';

export default {
    components: { BaseTitle, BaseRadioButton, MemberInfo, MemberAuctionItem, MemberBidding },
    data() {
        return {
            page: 'MemberInfo',
        };
    },
    created() {
        const savedPage = localStorage.getItem('selectedPage');
        if (savedPage) {
            this.page = savedPage;
        }
    },
    watch: {
        page(newPage) {
            localStorage.setItem('selectedPage', newPage);
        },
    },
};
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.radio-group {
    display: flex;
    justify-content: space-between;
    width: 1000px;
}
</style>
