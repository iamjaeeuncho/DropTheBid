<template>
    <div div class="container">
        <base-title title="로그인"></base-title>
        <v-form @submit.prevent="handleSignIn" ref="signInForm">
            <base-input label="아이디 (이메일)" type="email" v-model="signInForm.email" :rules="rules.email" autocomplete="email" />
            <base-input label="비밀번호" type="password" v-model="signInForm.password" :rules="[rules.password]" autocomplete="password" />

            <div class="error-container">
                <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
            </div>

            <base-button buttonName="로그인" type="submit" backgroundColor="#000" color="#fff" borderColor="#000"></base-button>
            <base-button buttonName="회원가입" to="/signup" backgroundColor="#fff" color="#000" borderColor="#000"></base-button>
        </v-form>
    </div>
</template>

<script>
import axios from 'axios';
import { mapActions } from 'vuex';
import BaseTitle from './atoms/BaseTitle.vue';
import BaseInput from './atoms/BaseInput.vue';
import BaseButton from './atoms/BaseButton.vue';

const signIn = async (signInForm) => {
    const res = await axios.post('/members/signin', signInForm, {
        withCredentials: true,
    });

    return res.data;
};

export default {
    components: { BaseInput, BaseButton, BaseTitle },
    data() {
        return {
            signInForm: { email: '', password: '' },
            errorMessage: '',
            rules: {
                email: [(value) => !!value || '이메일을 입력해주세요.'],
                password: (value) => !!value || '비밀번호를 입력해주세요.',
            },
        };
    },
    methods: {
        handleSignIn: function (e) {
            e.preventDefault();

            this.errorMessage = '';

            if (!this.$refs.signInForm.validate()) {
                return;
            }

            signIn(this.signInForm)
                .then((result) => {
                    const token = {
                        access_token: result,
                    };

                    this.login(token);

                    this.$router.push('/');
                })
                .catch((error) => {
                    console.log(error.response.data.message);
                    this.errorMessage = error.response.data.message || '로그인에 실패했습니다. 다시 시도해주세요.';
                });
        },

        ...mapActions({ login: 'member/login' }),
    },
};
</script>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.error-container {
    min-height: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
}
.error-message {
    color: red;
    font-size: 12px;
}
</style>
