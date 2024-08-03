<template>
  <div class="container">
      <base-title title="경매 상품 등록"></base-title>
        <auction-form-compo/>
  </div>
</template>

<script>
import AuctionFormCompo from '@/components/auction/AuctionFormCompo.vue'
import BaseTitle from '@/components/member/atoms/BaseTitle.vue';

export default {
    components:{
        AuctionFormCompo,
        BaseTitle,
    }
}
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