<template>
    <div div class="container">
        <v-form @submit.prevent="handleUpdateInfo" ref="memberInfoForm">
            <base-input label="아이디 (이메일)" type="email" v-model="memberInfoForm.email" :disabled="true" autocomplete="off" width="800px" />
            <base-input label="비밀번호" type="password" v-model="memberInfoForm.password" :rules="[rules.password]" autocomplete="off" width="800px" />
            <base-input
                label="비밀번호 확인"
                type="password"
                v-model="memberInfoForm.passwordCheck"
                :rules="[rules.passwordCheck]"
                autocomplete="off"
                width="800px"
                ref="passwordCheck"
            />
            <base-input label="닉네임" type="text" v-model="memberInfoForm.nickname" :disabled="true" autocomplete="off" width="800px" />
            <base-input label="이름" type="text" v-model="memberInfoForm.name" :rules="[rules.name]" autocomplete="off" width="800px" />
            <base-input label="생년월일 (8자리)" type="text" v-model="memberInfoForm.birthday" :rules="[rules.birthday]" autocomplete="off" width="800px" />

            <div class="radio-group">
                <base-radio-button label="남자" targetValue="m" v-model="memberInfoForm.sex"></base-radio-button>
                <base-radio-button label="여자" targetValue="w" v-model="memberInfoForm.sex"></base-radio-button>
            </div>

            <base-input label="주소" type="text" v-model="memberInfoForm.address" :rules="[rules.address]" autocomplete="off" width="800px" />

            <base-button buttonName="수정하기" type="submit" width="800px" backgroundColor="#000" color="#fff" borderColor="#000"></base-button>

            <base-button buttonName="탈퇴하기" type="button" @click="handleUpdateWithdrawal" width="800px" backgroundColor="#fff" color="#000" borderColor="#000"></base-button>
        </v-form>
    </div>
</template>

<script>
import axiosInstance from '../../utils/axiosinstance';
import BaseInput from './atoms/BaseInput.vue';
import BaseButton from './atoms/BaseButton.vue';
import BaseRadioButton from './atoms/BaseRadioButton.vue';

const loadMemberInfo = async () => {
    const res = await axiosInstance.get('/members/info');

    return res.data;
};

const updateMemberInfo = async (memberInfoForm) => {
    const res = await axiosInstance.put('/members/info', memberInfoForm);

    return res.data;
};

const updateMemberWithdrawal = async () => {
    const res = await axiosInstance.put('/members/info/withdrawal');

    return res.data;
};

const logout = async () => {
    const res = await axiosInstance.put('/members/logout');

    return res.data;
};

export default {
    components: { BaseInput, BaseButton, BaseRadioButton },
    data() {
        return {
            memberInfoForm: {
                email: '',
                password: '',
                passwordCheck: '',
                nickname: '',
                name: '',
                sex: '',
                birthday: '',
                address: '',
            },
            rules: {
                password: (value) => !!value || '비밀번호를 입력해주세요.',
                passwordCheck: (value) => {
                    if (!value) {
                        return '비밀번호 확인을 입력해주세요.';
                    }
                    if (value !== this.memberInfoForm.password) {
                        return '비밀번호가 일치하지 않습니다.';
                    }
                    return true;
                },
                name: (value) => !!value || '이름을 입력해주세요.',
                birthday: (value) => {
                    if (!value) {
                        return '생년월일을 입력해주세요.';
                    }
                    const pattern = /^\d{8}$/;
                    return pattern.test(value) || '생년월일은 8자리 숫자로 입력해주세요.';
                },
                address: (value) => !!value || '주소를 입력해주세요.',
            },
        };
    },
    watch: {
        'memberInfoForm.password': function () {
            this.validatePasswordCheck();
        },
    },
    created() {
        this.loadInfo();
    },
    methods: {
        validatePasswordCheck() {
            const passwordCheckInput = this.$refs.passwordCheck;
            if (passwordCheckInput) {
                passwordCheckInput.validate();
            }
        },
        async loadInfo() {
            try {
                const memberInfo = await loadMemberInfo();

                this.memberInfoForm = {
                    email: memberInfo.email,
                    password: '',
                    passwordCheck: '',
                    nickname: memberInfo.nickname,
                    name: memberInfo.name,
                    sex: memberInfo.sex,
                    birthday: memberInfo.birthday,
                    address: memberInfo.address,
                };
            } catch (error) {
                console.log(error);
            }
        },
        handleUpdateInfo: function (e) {
            e.preventDefault();

            if (!this.$refs.memberInfoForm.validate()) {
                return;
            }

            updateMemberInfo(this.memberInfoForm)
                .then(() => {
                    logout()
                        .then(() => {
                            this.$store.dispatch('member/logout');
                            if (this.$router.currentRoute.path !== '/') {
                                this.$router.push('/');
                            }
                        })
                        .catch((error) => {
                            console.log(error);
                        });
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        handleUpdateWithdrawal: function () {
            updateMemberWithdrawal()
                .then(() => {
                    logout()
                        .then(() => {
                            this.$store.dispatch('member/logout');
                            if (this.$router.currentRoute.path !== '/') {
                                this.$router.push('/');
                            }
                        })
                        .catch((error) => {
                            console.log(error);
                        });
                })
                .catch((error) => {
                    console.log(error);
                });
        },
    },
};
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
}
.radio-group {
    display: flex;
    gap: 10px;
    margin-bottom: 30px;
}
</style>
