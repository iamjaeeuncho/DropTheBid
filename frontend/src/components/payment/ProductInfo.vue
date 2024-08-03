<template>
  <div>
    <div class="middletitle">상품정보</div>
    <table>
      <tr>
        <td class="imgcell" rowspan="5">
          <img class="productimg" :src="imageSrc" alt="Product Image">
        </td>
        <td>{{ itemName }}</td>
        <td class="alignright">{{ price }}원</td>
      </tr>
      <tr>
        <td colspan="2">{{ categoryName }}</td>
      </tr>
      <tr>
        <td colspan="2">경매종료일자: {{ dueDate }}</td> 
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ProductInfo',
  props: {
    imageSrc: {
      type: String,
      required: true
    },
    itemName: {
      type: String,
      required: true
    },
    price: {
      type: Number,
      required: true
    },
    categoryName: {
      type: String,
      required: true
    },
    dueDate: {  
      type: String,
      required: true
    },
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
  padding: 10px 20px;
  border: none !important;
}

.imgcell {
  width: 250px;
  text-align: center;
}

.productimg {
  width: 200px;
  height: 200px;
}
</style>
