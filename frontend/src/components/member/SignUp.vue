<template>
    <div div class="container">
        <base-title title="회원가입"></base-title>

        <v-form @submit.prevent="handleSignUp" ref="signUpForm">
            <base-input
                label="아이디 (이메일)"
                type="email"
                v-model="signUpForm.email"
                :rules="[rules.email]"
                :error-message="errorMessages.email"
                autocomplete="off"
                width="800px"
            />
            <base-input label="비밀번호" type="password" v-model="signUpForm.password" :rules="[rules.password]" autocomplete="off" width="800px" />
            <base-input
                label="비밀번호 확인"
                type="password"
                v-model="signUpForm.passwordCheck"
                :rules="[rules.passwordCheck]"
                autocomplete="off"
                width="800px"
                ref="passwordCheck"
            />
            <base-input
                label="닉네임"
                type="text"
                v-model="signUpForm.nickname"
                :rules="[rules.nickname]"
                :error-message="errorMessages.nickname"
                autocomplete="off"
                width="800px"
            />
            <base-input label="이름" type="text" v-model="signUpForm.name" :rules="[rules.name]" autocomplete="off" width="800px" />
            <base-input label="생년월일 (8자리)" type="text" v-model="signUpForm.birthday" :rules="[rules.birthday]" autocomplete="off" width="800px" />

            <div class="radio-group">
                <base-radio-button label="남자" targetValue="m" v-model="signUpForm.sex" :error="blRulesMessage && !signUpForm.sex"></base-radio-button>
                <base-radio-button label="여자" targetValue="w" v-model="signUpForm.sex" :error="blRulesMessage && !signUpForm.sex"></base-radio-button>
            </div>
            <div class="radio-rules-container">
                <div v-if="rulesMessage" class="radio-rules-message">{{ rulesMessage }}</div>
            </div>

            <base-input label="주소" type="text" v-model="signUpForm.address" :rules="[rules.address]" autocomplete="off" width="800px" />

            <base-button buttonName="가입하기" type="submit" width="800px" backgroundColor="#000" color="#fff" borderColor="#000"></base-button>
        </v-form>
    </div>
</template>

<script>
import axios from 'axios';
import BaseTitle from './atoms/BaseTitle.vue';
import BaseInput from './atoms/BaseInput.vue';
import BaseButton from './atoms/BaseButton.vue';
import BaseRadioButton from './atoms/BaseRadioButton.vue';

const signUp = async (signUpForm) => {
    const res = await axios.post('/members/signup', signUpForm, {
        withCredentials: true,
    });

    return res.data;
};

export default {
    components: { BaseInput, BaseButton, BaseTitle, BaseRadioButton },
    data() {
        return {
            signUpForm: { email: '', password: '', passwordCheck: '', nickname: '', name: '', sex: '', birthday: '', address: '' },
            errorMessages: {
                email: '',
                nickname: '',
            },
            rulesMessage: '',
            blRulesMessage: false,
            rules: {
                email: (value) => {
                    if (!value) {
                        return '이메일을 입력해주세요.';
                    }
                    const pattern =
                        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                    return pattern.test(value) || '이메일형식이 잘못되었습니다.';
                },
                password: (value) => !!value || '비밀번호를 입력해주세요.',
                passwordCheck: (value) => {
                    if (!value) {
                        return '비밀번호 확인을 입력해주세요.';
                    }
                    if (value !== this.signUpForm.password) {
                        return '비밀번호가 일치하지 않습니다.';
                    }
                    return true;
                },
                nickname: (value) => !!value || '닉네임을 입력해주세요.',
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
        'signUpForm.sex': function (newVal) {
            if (newVal) {
                this.rulesMessage = '';
                this.blRulesMessage = false;
            }
        },
        'signUpForm.email': function () {
            this.errorMessages.email = '';
        },
        'signUpForm.password': function () {
            this.validatePasswordCheck();
        },
        'signUpForm.nickname': function () {
            this.errorMessages.nickname = '';
        },
    },
    methods: {
        validatePasswordCheck() {
            const passwordCheckInput = this.$refs.passwordCheck;
            if (passwordCheckInput) {
                passwordCheckInput.validate();
            }
        },
        handleSignUp: function (e) {
            e.preventDefault();

            if (!this.$refs.signUpForm.validate() || !this.signUpForm.sex) {
                if (!this.signUpForm.sex) {
                    this.blRulesMessage = true;
                    this.rulesMessage = '성별을 선택해주세요.';
                }
                return;
            }

            signUp(this.signUpForm)
                .then(() => {
                    this.$router.push('/signin');
                })
                .catch((error) => {
                    const errorData = error.response.data;
                    if (errorData.errorCode.includes('EMAIL')) {
                        this.errorMessages.email = error.response.data.message;
                    }
                    if (errorData.errorCode.includes('NICKNAME')) {
                        this.errorMessages.nickname = error.response.data.message;
                    }
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
}
.radio-group {
    display: flex;
    gap: 10px;
}
.radio-rules-container {
    min-height: 25px;
    display: flex;
}
.radio-rules-message {
    color: red;
    font-size: 12px;
    margin-left: 10px;
    opacity: 0;
    transform: translateY(-5px);
    animation: fadeInDown 0.5s forwards;
}

@keyframes fadeInDown {
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
