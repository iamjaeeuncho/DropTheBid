<template>
  <div>
    <div class="middletitle">결제정보</div>
    <table>
      <tr class="leftwidth">
        <td>상품</td>
        <td class="alignright">{{ price }}원</td>
      </tr>
      <tr>
        <td>배송비</td>
        <td class="alignright">{{ shippingFee }}원</td>
      </tr>
      <tr>
        <td>최종 결제 금액</td>
        <td class="alignright">{{ totalprice }}원</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'PaymentInfo',
  props: {
    price: {
      type: Number,
      required: true
    },
    shippingFee: {
      type: Number,
      required: true
    },
    totalprice: {
      type: Number,
      required: true
    }
  }
}
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  padding: 10px;
  margin: 20px 0;
}

tr, td {
  height: 40px !important;
  padding: 10px 20px;
  border: none !important;
}

.leftwidth {
  width: 250px;
}


</style>
