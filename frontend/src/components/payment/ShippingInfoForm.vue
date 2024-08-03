<template>
  <div>
    <div class="middletitle">배송정보</div>
    <table>
      <tr class="leftwidth">
        <td><label for="name">이름</label></td>
        <td><input type="text" id="name" v-model="localForm.name" class="form-control inputform" placeholder="이름을 입력해주세요" required></td>
      </tr>
      <tr>
        <td><label for="phone">전화번호</label></td>
        <td><input type="tel" id="phone" v-model="localForm.phone" class="form-control inputform" placeholder="전화번호를 입력해주세요" required></td>
      </tr>
      <tr>
        <td><label for="address">배송지</label></td>
        <td><input type="text" id="address" v-model="localForm.address" class="form-control inputform" placeholder="주소를 입력해주세요" required></td>
      </tr>
      <tr>
        <td><label for="message">배송메세지</label></td>
        <td><input type="text" id="message" v-model="localForm.message" class="form-control inputform" placeholder="메세지를 입력해주세요"></td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ShippingInfoForm',
  props: {
    form: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      localForm: { ...this.form }
    };
  },
  watch: {
    localForm: {
      handler(newValue) {
        this.$emit('update:form', newValue);
      },
      deep: true
    }
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

.inputform {
  width: 100%;
  height: 40px;
  border: 1px solid #999999;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 10px;
}

</style>
