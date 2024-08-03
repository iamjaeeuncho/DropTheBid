<template>
  <div>
    <div class="middletitle">배송정보</div>
    <table>
      <tr class="leftwidth">
        <td><label for="name">이름</label></td>
        <td class="alignright">{{ localForm.name }}</td>
      </tr>
      <tr>
        <td><label for="phone">전화번호</label></td>
        <td class="alignright">{{ localForm.phone }}</td>
      </tr>
      <tr>
        <td><label for="address">배송지</label></td>
        <td class="alignright">{{ localForm.address }}</td>
      </tr>
      <tr>
        <td><label for="message">배송메세지</label></td>
        <td class="alignright">{{ localForm.message }}</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ShippingInfoStatus',
  data() {
    return {
      localForm: JSON.parse(localStorage.getItem('formData')) || {}
    };
  }
}
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
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
