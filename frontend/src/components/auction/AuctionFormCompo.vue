<template>
    <form @submit="handleSubmitAuction">
        <auction-input-compo label="상품명">
            <auction-text-field-compo v-model="auctionForm.itemName" />
        </auction-input-compo>

        <auction-input-compo label="카테고리">
            <auction-select-compo v-model="auctionForm.categoryId" :items="categoryItems" />
        </auction-input-compo>

        <auction-input-compo label="상품 상태">
            <auction-select-compo v-model="auctionForm.auctionProductStatusId" :items="productStatusItems" />
        </auction-input-compo>

        <auction-input-compo label="상품 위치">
            <auction-text-field-compo v-model="auctionForm.location" />
        </auction-input-compo>

        <auction-input-compo label="시작 가격">
            <auction-text-field-compo v-model="auctionForm.startPrice" type="number" min="0" />
        </auction-input-compo>

        <auction-input-compo label="판매 기간">
            <auction-text-field-compo v-model="auctionForm.startDate" type="datetime-local" :min="currentDateTime" @change="validateStartDate" />
            <auction-text-field-compo v-model="auctionForm.dueDate" type="datetime-local" :min="startDateTime" @change="validateDueDate" />
        </auction-input-compo>

        <auction-input-compo label="설명">
            <auction-text-area-compo v-model="auctionForm.description"></auction-text-area-compo>
        </auction-input-compo>

        <auction-input-compo label="사진 등록">
            <auction-file-compo multiple accept="image/*" buttonText="이미지 선택" hint="* 대표 사진을 선택해주세요." @files-selected="onFileChange" />
        </auction-input-compo>
        <auction-input-compo>
            <div v-if="imagePreviews.length">
                <div class="image-preview-container">
                    <div v-for="(image, index) in imagePreviews" :key="index" class="image-preview">
                        <button type="button" class="remove-button" @click="removeImage(index)">X</button>
                        <img :src="image" :alt="'미리보기 ' + (index + 1)" width="100" height="100" @click="setMainImage(index)" />
                        <label id="main-image-label" v-if="mainImageIndex === index">대표 사진</label>
                    </div>
                </div>
            </div>
        </auction-input-compo>
        <BaseButton type="submit" :disabled="isSubmitting" :buttonName="buttonText" backgroundColor="#000" color="#fff" borderColor="#000"></BaseButton>
    </form>
</template>

<script>
import axiosInstance from '@/utils/axiosinstance';
import _ from 'lodash';
import BaseButton from '@/components/member/atoms/BaseButton.vue';
import AuctionInputCompo from './atoms/AuctionInputCompo.vue';
import AuctionTextFieldCompo from './atoms/AuctionTextFieldCompo';
import AuctionSelectCompo from './atoms/AuctionSelectCompo.vue';
import AuctionTextAreaCompo from './atoms/AuctionTextAreaCompo.vue';
import AuctionFileCompo from './atoms/AuctionFileCompo.vue';

const registerAuction = async (auctionForm, images, mainImageIndex) => {
    const formData = new FormData();

    formData.append('newAuction', JSON.stringify(auctionForm));
    images.forEach((image) => {
        formData.append('images', image);
    });
    formData.append('mainImageIndex', mainImageIndex);
    // console.log(formData.get('mainImageIndex'));
    // console.log(typeof formData.get('mainImageIndex'));

    try {
        const res = await axiosInstance.post('/auctions', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            withCredentials: true,
        });
        if (res.status === 201) {
            auctionForm.auctionId = res.data;
            return true;
        } else {
            console.log(res.statusText);
            return false;
        }
    } catch (error) {
        console.error('Axios Error:', error);
        return false;
    }
};

const updateAuction = async (auctionForm, images, mainImageIndex, deletedImages, initialAuctionData, initialMainImageIndex) => {
    const formData = new FormData();

    // 변경된 필드만 포함한 객체 생성
    const updatedFields = { auctionId: auctionForm.auctionId };
    let hasUpdatedFields = false;
    for (const key in auctionForm) {
        console.log('Checking key:', key);
        console.log('auctionForm[key]:', auctionForm[key]);
        console.log('initialAuctionData[key]:', initialAuctionData[key]);
        if (auctionForm[key] !== initialAuctionData[key]) {
            updatedFields[key] = auctionForm[key];
            hasUpdatedFields = true;
        }
    }
    console.log(updatedFields);
    if (hasUpdatedFields) {
        formData.append('modifiedAuction', JSON.stringify(updatedFields));
    }

    images.forEach((image) => {
        if (image instanceof File) {
            formData.append('newImages', image);
        }
    });
    if (deletedImages && deletedImages.length > 0) {
        formData.append('deletedImages', JSON.stringify(deletedImages));
    }
    console.log(initialMainImageIndex);
    console.log(mainImageIndex);
    if (initialMainImageIndex !== mainImageIndex) {
        formData.append('mainImageIndex', mainImageIndex);
    }
    try {
        const res = await axiosInstance.put(`/auctions/${auctionForm.auctionId}`, formData, {
            withCredentials: true,
        });
        if (res.status === 200) {
            return true;
        } else {
            console.log(res.statusText);
            return false;
        }
    } catch (error) {
        console.error('Axios Error:', error);
        return false;
    }
};

export default {
    props: ['initialAuctionData', 'initialMainImageIndex', 'initialAuctionImages', 'isEditMode'],
    data() {
        return {
            auctionForm: {
                auctionId: '',
                itemName: '',
                categoryId: null,
                auctionProductStatusId: null,
                location: '',
                startPrice: null,
                startDate: '',
                dueDate: '',
                description: '',
            },
            images: [],
            imagePreviews: [], // 이미지 미리보기
            newImagePreviews: [],
            deletedImages: [],
            mainImageIndex: null,
            isSubmitting: false,
            categoryItems: [
                { value: 1, text: '패션' },
                { value: 2, text: '가전' },
                { value: 3, text: '스포츠' },
                { value: 4, text: '수집품' },
                { value: 5, text: '기타' },
            ],
            productStatusItems: [
                { value: 1, text: '중고' },
                { value: 2, text: '새상품' },
                { value: 3, text: '미개봉' },
            ],
        };
    },
    components: {
        BaseButton,
        AuctionInputCompo,
        AuctionTextFieldCompo,
        AuctionSelectCompo,
        AuctionTextAreaCompo,
        AuctionFileCompo,
    },
    mounted() {
        console.log('Initial auction data:', this.initialAuctionData);
        console.log('Initial images:', this.initialAuctionImages);
        console.log('Initial main image index:', this.initialMainImageIndex);
        if (this.isEditMode) {
            this.auctionForm = _.cloneDeep(this.initialAuctionData);
            this.images = _.cloneDeep(this.initialAuctionImages);
            this.imagePreviews = _.cloneDeep(this.initialAuctionImages);
            this.auctionForm.startDate = this.formatToDatetimeLocal(this.initialAuctionData.startDate);
            this.auctionForm.dueDate = this.formatToDatetimeLocal(this.initialAuctionData.dueDate);
            this.mainImageIndex = this.initialMainImageIndex;
            console.log('Formatted auction data:', this.auctionForm);
        }
    },
    methods: {
        onFileChange(files, event) {
            const selectedFiles = Array.from(files);
            let totalFiles = 0;
            totalFiles = this.images.length + selectedFiles.length;
            if (totalFiles > 10) {
                alert('이미지는 최대 10장까지 추가할 수 있습니다.');
                event.target.value = null;
                return;
            }
            selectedFiles.forEach((file) => {
                this.imagePreviews.push(URL.createObjectURL(file));
            });
            this.images = [...this.images, ...selectedFiles];
            event.target.value = null;
        },
        handleSubmitAuction(e) {
            e.preventDefault();
            if (this.imagePreviews.length === 0) {
                alert('사진은 한 장 이상 등록해야합니다.');
                return;
            }
            if (this.mainImageIndex == null) {
                alert('대표 사진을 선택해주세요.');
                return;
            }
            this.isSubmitting = true;

            this.auctionForm.startDate = this.convertToUTC(this.auctionForm.startDate);
            this.auctionForm.dueDate = this.convertToUTC(this.auctionForm.dueDate);
            console.log(this.auctionForm.startDate);
            console.log(this.auctionForm.dueDate);

            if (!this.isEditMode) {
                registerAuction(this.auctionForm, this.images, this.mainImageIndex).then((result) => {
                    this.isSubmitting = false;
                    if (result) {
                        alert('상품 등록이 완료되었습니다.');
                        this.$router.push({ name: 'HomePage' });
                    } else {
                        alert('서버 오류입니다. 관리자에게 문의해주세요.');
                    }
                });
            } else {
                const changedMainImageIndex = this.mainImageIndex;
                updateAuction(this.auctionForm, this.images, changedMainImageIndex, this.deletedImages, this.initialAuctionData, this.initialMainImageIndex).then((result) => {
                    this.isSubmitting = false;
                    if (result) {
                        alert('상품 수정이 완료되었습니다.');
                        this.$router.push({ name: 'MyPage' });
                    } else {
                        alert('서버 오류입니다. 관리자에게 문의해주세요.');
                    }
                });
            }
        },
        convertToUTC(date) {
            if (!date) return '';
            const localDate = new Date(date);
            return localDate.toISOString().slice(0, 16);
        },
        validateStartDate() {
            const startDate = new Date(this.auctionForm.startDate);
            const currentDate = new Date(this.currentDateTime);
            if (startDate < currentDate) {
                alert('시작 시간은 현재 시간 이후여야 합니다.');
                this.auctionForm.startDate = '';
            }
        },
        validateDueDate() {
            const startDate = new Date(this.auctionForm.startDate);
            const dueDate = new Date(this.auctionForm.dueDate);
            if (dueDate <= startDate) {
                alert('종료 날짜는 시작 날짜 이후여야 합니다.');
                this.auctionForm.dueDate = '';
            }
        },
        removeImage(index) {
            console.log(index);
            const removedImage = this.imagePreviews[index];
            this.images.splice(index, 1);
            this.imagePreviews.splice(index, 1);
            this.deletedImages.push(removedImage);
            if (this.mainImageIndex == index) {
                this.mainImageIndex = null;
            }
            console.log(this.mainImageIndex);
        },
        setMainImage(index) {
            // if (this.isEditMode){
            //   this.mainImageIndex = index-(this.imagePreviews.length-this.images.length);
            // } else {
            //   this.mainImageIndex = index;
            // }
            this.mainImageIndex = index;
            console.log(this.mainImageIndex);
        },
        formatToDatetimeLocal(dateString) {
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            return `${year}-${month}-${day}T${hours}:${minutes}`;
        },
    },
    computed: {
        currentDateTime() {
            const now = new Date();
            const utcDate = new Date(now.getTime() - now.getTimezoneOffset() * 60000);
            return utcDate.toISOString().slice(0, 16);
        },
        startDateTime() {
            return this.auctionForm.startDate ? this.auctionForm.startDate : this.currentDateTime;
        },
        buttonText() {
            return this.isEditMode ? '수정하기' : '등록하기';
        },
    },
};
</script>

<style scoped>
.image-preview-container {
    display: flex;
    flex-wrap: wrap;
}
.image-preview {
    position: relative;
    display: inline-block;
    margin: 7px;
}
.remove-button {
    position: absolute;
    top: -5px;
    right: -5px;
    background: white;
    border: 2px solid black;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}
#main-image-label {
    position: absolute;
    bottom: 6px;
    right: 0px;
    height: 20px;
    width: 100px;
    color: white;
    text-align: center;
    font-size: 15px;
    background-color: #000;
    opacity: 0.5;
}
form {
    display: flex;
    flex-direction: column;
    align-items: center; /* 버튼을 가운데 정렬 */
}
</style>
