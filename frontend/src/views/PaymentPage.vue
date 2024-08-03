<template>
    <div class="container" v-if="isAuctionDataLoaded">
        <div class="maintitle aligncenter">결제하기</div>
        <ProductInfo
            :price="price"
            :imageSrc="auctionData.images"
            :mainImageIndex="mainImageIndex"
            :itemName="auctionData.itemName"
            :categoryName="auctionData.categoryName"
            :dueDate="auctionData.dueDate"
        />
        <hr class="line" />
        <ShippingInfoForm :form.sync="form" />
        <hr class="line" />
        <PaymentInfo :price="price" :shippingFee="shippingFee" :totalprice="totalprice" />
        <div class="actioncell">
            <v-btn class="actionbutton" type="submit" @click="submitForm">결제하기</v-btn>
        </div>
    </div>
    <div v-else>
        <p>Loading...</p>
    </div>
</template>

<script>
import axiosInstance from '@/utils/axiosinstance.js';
import ProductInfo from '@/components/payment/ProductInfo.vue';
import ShippingInfoForm from '@/components/payment/ShippingInfoForm.vue';
import PaymentInfo from '@/components/payment/PaymentInfo.vue';

export default {
    name: 'PaymentPage',
    components: {
        ProductInfo,
        ShippingInfoForm,
        PaymentInfo,
    },
    data() {
        return {
            merchantUid: 'order-' + new Date().getTime(),
            price: 0,
            shippingFee: 300,
            form: {
                name: '',
                phone: '',
                address: '',
                message: '',
            },
            auctionData: {
                itemName: '',
                images: [],
                categoryName: '',
                dueDate: '',
            },
            mainImageIndex: 0,
            biddingData: {
                price: 0,
            },
            isAuctionDataLoaded: false,
        };
    },
    computed: {
        totalprice() {
            return this.price + this.shippingFee;
        },
    },
    methods: {
        async setAuctionData() {
            try {
                const auctionId = this.$route.params.id;
                const res = await axiosInstance.post(
                    '/payment',
                    { auctionId },
                    {
                        headers: {
                            'Content-Type': 'application/json',
                            withCredentials: true,
                        },
                    },
                );

                if (res.status === 200) {
                    const { auctionData, images, mainImageIndex, biddingData } = res.data;
                    this.auctionData = {
                        itemName: auctionData.itemName,
                        images: images,
                        categoryName: auctionData.categoryName,
                        dueDate: auctionData.dueDate,
                    };
                    this.mainImageIndex = mainImageIndex;
                    this.biddingData = biddingData || {};
                    this.price = this.biddingData.price || 0;
                    this.isAuctionDataLoaded = true;
                } else {
                    console.log(res.statusText);
                }
            } catch (error) {
                console.error('Error:', error);
            }
        },
        submitForm() {
            const { merchantUid, totalprice, form } = this;

            window.IMP.init('imp30164472');

            window.IMP.request_pay(
                {
                    pg: 'html5_inicis',
                    pay_method: 'card',
                    merchant_uid: merchantUid,
                    name: this.auctionData.itemName || 'Unknown Item',
                    amount: totalprice,
                    buyer_email: 'redjoun@gmail.com',
                    buyer_name: form.name,
                    buyer_tel: form.phone,
                    buyer_addr: form.address,
                },
                (rsp) => {
                    if (rsp.success) {
                        // 결제 성공 시
                        console.log(rsp);
                        alert('결제가 완료되었습니다.');
                        window.location.href = '/';

                        // Save form data to localStorage
                        localStorage.setItem('formData', JSON.stringify(form));

                        fetch(
                            `/payment/save`,
                            {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                auctionId: this.$route.params.id,
                                biddingSuccessId: 1,
                                name: form.name,
                                phoneNumber: form.phone,
                                address: form.address,
                                message: form.message,
                            },
                            {
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                            },
                        );
                        // .then(response => {
                        //   if (response.status === 200) {
                        //     window.location.href = '/auctions/paymentstatus';
                        //   } else {
                        //     alert('결제 정보 업데이트에 실패했습니다.');
                        //   }
                        // })
                        // .catch((error) => {
                        //     console.error('Error:', error);
                        //     alert('결제 정보 업데이트 중 오류가 발생했습니다.');
                        // });
                    } else {
                        // 결제 실패 시
                        console.log(rsp);
                        alert('결제에 실패하였습니다. 에러 내용: ' + rsp.error_msg);
                    }
                },
            );
        },
    },
    mounted() {
        this.setAuctionData();
    },
};
</script>

<style scoped>
@import '@/assets/style.css';

.actionbutton {
    margin: 10px;
    width: 400px;
}

.actioncell {
    text-align: center;
}

.line {
    border: 1px solid #ddd;
}
</style>
