<template>
  <div class="container">
    <div class="maintitle aligncenter">결제확인</div>
    <ProductInfo :price="price" imageSrc="@/assets/00.jpg" />
    <hr class="line"/>
    <ShippingInfoStatus :form.sync="form" />
    <hr class="line"/>
    <PaymentInfo :price="price" :shippingFee="shippingFee" :totalprice="totalprice" />
  </div>
</template>

<script>
import ProductInfo from '@/components/payment/ProductInfo.vue';
import ShippingInfoStatus from '@/components/payment/ShippingInfoStatus.vue';
import PaymentInfo from '@/components/payment/PaymentInfo.vue';

export default {
  name: 'PaymentStatusPage',
  components: {
    ProductInfo,
    ShippingInfoStatus,
    PaymentInfo
  },
  data() {
    return {
      price: 100,
      shippingFee: 300,
    };
  },
  computed: {
    totalprice() {
      return this.price + this.shippingFee;
    }
  },
  methods: {
  }
}
</script>

<style scoped>
@import '@/assets/style.css';

.line {
  border: 1px solid #ddd;
}

</style>
