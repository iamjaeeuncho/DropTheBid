<template>
    <div class="container" v-if="isAuctionDataLoaded && isBiddingDataLoaded">
        <div class="tinytitle">카테고리 > {{ auctionData.categoryName }}</div>
        <div class="maintitle spacebetween">
            <span
                >{{ auctionData.itemName }}
                <span :class="statusClass">{{ auctionData.auctionStatusName }}</span>
            </span>
            <span>
                <v-btn icon class="ml-4">
                    <v-icon @click="navigateToChat">mdi-chat-outline</v-icon>
                </v-btn>
            </span>
        </div>

        <table>
            <tr>
                <td rowspan="8" class="leftwidth">
                    <v-carousel cycle hide-delimiter-background show-arrows-on-hover class="custom-carousel">
                        <v-carousel-item v-for="(image, i) in images" :key="i" :src="image" class="carousel-item"> </v-carousel-item>
                    </v-carousel>
                </td>
                <td class="aligncenter">상품상태</td>
                <td class="alignright">{{ auctionData.auctionProductStatusName }}</td>
            </tr>
            <tr>
                <td class="aligncenter">상품위치</td>
                <td class="alignright">{{ auctionData.location }}</td>
            </tr>
            <tr>
                <td class="aligncenter">거래방법</td>
                <td class="alignright">택배</td>
            </tr>
            <tr>
                <td class="aligncenter">시작가격</td>
                <td class="alignright">{{ auctionData.startPrice }}원</td>
            </tr>
            <tr>
                <td class="aligncenter">현재가격</td>
                <td class="alignright">
                    <p v-if="biddingData.length === 0">{{ auctionData.startPrice }}원</p>
                    <p v-else>{{ biddingData[0].price }}원</p>
                </td>
            </tr>
            <tr>
                <td class="aligncenter">판매기간</td>
                <td class="alignright">
                    {{ formattedStartDate }}<br />
                    ~ {{ formattedDueDate }}<br />
                    <p v-if="auctionData.auctionStatusId === 2">{{ remainingTime }}</p>
                </td>
            </tr>
            <tr>
                <td class="aligncenter">입찰상태</td>
                <td class="alignright">{{ biddingData.length }} bids</td>
            </tr>
            <tr v-if="auctionData.auctionStatusId === 2">
                <td class="aligncenter" colspan="2">
                    <v-dialog v-model="dialog" max-width="500px">
                        <template v-slot:activator="{ on }">
                            <base-button buttonName="입찰하기" type="submit" backgroundColor="#000" color="#fff" borderColor="#000" v-on="on" @click="dialog = true"></base-button>
                        </template>
                        <v-card>
                            <v-card-title class="headline">
                                <span class="headline">입찰신청</span>
                                <v-spacer></v-spacer>
                                <v-btn icon @click="dialog = false">
                                    <v-icon>mdi-close</v-icon>
                                </v-btn>
                            </v-card-title>
                            <v-divider></v-divider>
                            <v-spacer></v-spacer>
                            <v-card-text class="py-3">
                                <v-form>
                                    <v-row class="my-1">
                                        <v-col cols="5">
                                            <div class="subtitle-1">현재 입찰가</div>
                                        </v-col>
                                        <v-col cols="7">
                                            <div v-if="biddingData.length === 0" class="text-right">
                                                <h3>{{ auctionData.startPrice }}원</h3>
                                            </div>
                                            <div v-else class="text-right">
                                                <h3>{{ biddingData[0].price }}원</h3>
                                            </div>
                                        </v-col>
                                    </v-row>
                                    <v-row class="my-1">
                                        <v-col cols="5">
                                            <div class="subtitle-1">입찰가</div>
                                        </v-col>
                                        <v-col cols="7">
                                            <v-text-field outlined v-model="newBidingPrice" :error-messages="errorMessages" dense @input="validateBidPrice"></v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-form>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <div class="d-flex justify-center" style="width: 100%">
                                    <base-button
                                        buttonName="입찰하기"
                                        type="submit"
                                        backgroundColor="#000"
                                        color="#fff"
                                        borderColor="#000"
                                        width="300px"
                                        v-on="on"
                                        @click="submitBid()"
                                    ></base-button>
                                </div>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </td>
            </tr>
            <tr v-else></tr>
        </table>
        <div class="menu">
            <span class="middletitle" :class="{ unbold: selectedMenu !== 'description' }" @click="showAuctionDescription">상품설명&nbsp;&nbsp;&nbsp;</span>
            <span class="middletitle" :class="{ unbold: selectedMenu !== 'bidHistory' }" @click="showBidHistory">입찰내역</span>
        </div>
        <div v-if="selectedMenu === 'description'">{{ auctionData.description }}</div>
        <v-simple-table v-if="selectedMenu === 'bidHistory'" class="bid-history-table">
            <thead>
                <tr>
                    <th class="text-center">번호</th>
                    <th class="text-center">입찰가</th>
                    <th class="text-center">회원 ID</th>
                    <th class="text-center">시간</th>
                </tr>
            </thead>
            <tbody>
                <tr v-if="biddingData.length === 0">
                    <td colspan="4">입찰 내역이 없습니다.</td>
                </tr>
                <tr v-else v-for="(bidding, index) in biddingData" :key="index">
                    <td class="text-center">{{ index + 1 }}</td>
                    <td class="text-center">{{ bidding.price }}원</td>
                    <td class="text-center">{{ bidding.memberEmail }}</td>
                    <td class="text-center">{{ formatBidTime(bidding.time) }}</td>
                </tr>
            </tbody>
        </v-simple-table>
        <!-- 성공 메시지 모달 -->
        <v-dialog v-model="successDialog" max-width="500">
            <v-card>
                <v-card-title class="headline">입찰 완료</v-card-title>
                <v-card-text> 입찰이 성공적으로 완료되었습니다. </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="dark" @click="successDialog = false">확인</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
    <div v-else>
        <p>Loading...</p>
    </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment';
import axiosInstance from '../utils/axiosinstance';
import BaseButton from '@/components/member/atoms/BaseButton.vue';

// 회원 정보 로드
const loadMemberInfo = async () => {
  try {
    const res = await axiosInstance.get('/members/info');
    return res.data;
  } catch (error) {
    console.error('Failed to load member info:', error);
    throw error;
  }
};

export default {
    components: { BaseButton },
    name: 'AuctionDetailPage',
    data() {
        return {
            auctionData: {
                auctionId: this.$route.params.id,
                itemName: '',
                categoryName: '',
                auctionProductStatusName: '',
                auctionStatusName: '',
                auctionStatusId: '',
                location: '',
                startPrice: 0,
                startDate: '',
                dueDate: '',
                description: '',
            },
            biddingData: [],
            images: [],
            mainImageIndex: null,
            isAuctionDataLoaded: false,
            isBiddingDataLoaded: false,
            selectedMenu: 'description',
            bidHistory: '',
            remainingTime: '0일 0시간 0분 0초',
            dialog: false,
            newBidingPrice: null,
            successDialog: false,
            errorMessages: [],
            isBidPriceValid: false,
            memberId: null,
        };
    },
    methods: {
        async loadInfo() {
            try {
                const memberInfo = await loadMemberInfo();
                this.memberId = memberInfo.memberId;
            } catch (error) {
                console.error('Failed to load member info:', error);
            }
        },
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

                    console.log(this.auctionData);
                    this.isAuctionDataLoaded = true; // 데이터 로드 완료
                } else {
                    console.log(res.statusText);
                }
            } catch (error) {
                console.error('Axios Error:', error);
            }
        },
        async setBiddingData() {
            try {
                const res = await axios.get(`/auctions/all/${this.$route.params.id}/bids`);

                if (res.status === 200) {
                    console.log(res.data);
                    this.biddingData = res.data.biddingData;
                    this.isBiddingDataLoaded = true; // 데이터 로드 완료
                } else {
                    console.log(res.statusText);
                }
            } catch (error) {
                console.error('Axios Error:', error);
            }
        },
        async submitBid() {
            if (!this.isBidPriceValid) {
                return; // 유효하지 않은 경우, 제출하지 않음
            }

            const formData = new FormData();
            formData.append('price', this.newBidingPrice);

            try {
                const res = await axiosInstance.post(`/auctions/${this.$route.params.id}/bids`, formData);

                if (res.status === 200) {
                    console.log(res.data);
                    this.successDialog = true;
                    this.dialog = false;
                    this.setBiddingData(); // 입찰 데이터 새로고침
                } else {
                    console.log(res.statusText);
                }
            } catch (error) {
                console.error('Axios Error:', error);
            }
        },
        validateBidPrice() {
            this.errorMessages = [];
            this.isBidPriceValid = true;

            if (this.biddingData.length === 0) {
                if (this.newBidingPrice < this.auctionData.startPrice) {
                    this.errorMessages.push(`입찰가는 시작가(${this.auctionData.startPrice}) 이상이어야 합니다.`);
                    this.isBidPriceValid = false;
                }
            } else if (this.newBidingPrice <= this.biddingData[0].price) {
                this.errorMessages.push(`입찰가는 현재 입찰가(${this.biddingData[0].price})보다 커야 합니다.`);
                this.isBidPriceValid = false;
            }
        },
        updateRemainingTime() {
            if (!this.auctionData.dueDate) {
                this.remainingTime = '0일 0시간 0분 0초';
                return;
            }

            const now = moment();
            const dueDate = moment(this.auctionData.dueDate);
            const duration = moment.duration(dueDate.diff(now));

            if (duration.asMilliseconds() <= 0) {
                this.remainingTime = '0일 0시간 0분 0초';
            } else {
                const days = Math.floor(duration.asDays());
                const hours = duration.hours();
                const minutes = duration.minutes();
                const seconds = duration.seconds();
                this.remainingTime = `${days}일 ${hours}시간 ${minutes}분 ${seconds}초 남음`;
            }
        },
        startTimer() {
            this.timer = setInterval(this.updateRemainingTime, 1000);
        },
        stopTimer() {
            clearInterval(this.timer);
        },
        showBidHistory() {
            this.selectedMenu = 'bidHistory';
        },
        showAuctionDescription() {
            this.selectedMenu = 'description';
        },
        formatBidTime(time) {
            return moment(time).format('YYYY년 MM월 DD일 HH시 mm분');
        },
        navigateToChat() {
            this.$router.push(`/chat/${this.$route.params.id}/${this.memberId}`);
        },
    },
    mounted() {
        this.loadInfo();
        this.setAuctionData();
        this.setBiddingData();
        this.updateRemainingTime();
        this.startTimer();
    },
    beforeDestroy() {
        this.stopTimer();
    },
    computed: {
        formattedStartDate() {
            return moment(this.auctionData.startDate).format('YYYY년 MM월 DD일 HH시 mm분');
        },
        formattedDueDate() {
            return moment(this.auctionData.dueDate).format('YYYY년 MM월 DD일 HH시 mm분');
        },
        statusClass() {
            switch (this.auctionData.auctionStatusId) {
                case 1:
                    return 'beforebtn';
                case 2:
                    return 'ongoingbtn';
                case 3:
                case 4:
                case 5:
                    return 'finishbtn';
                default:
                    return '';
            }
        },
    },
};
</script>

<style>
@import '@/assets/style.css';

table {
    width: 100%;
    border-collapse: collapse;
}

tr,
td {
    height: 30px;
    padding: 0px;
    /* border: 1px solid black !important; */
}

.leftwidth {
    width: 500px;
}

.custom-carousel {
    width: 500px !important;
    height: 500px !important;
}

.carousel-item {
    width: 500px !important;
    height: 500px !important;
}

.middletitle {
    cursor: pointer;
}
.menu {
    margin-bottom: 10px;
}
</style>
