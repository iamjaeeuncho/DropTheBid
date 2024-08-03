const store = {
    namespaced: true,
    state() {
        return {
            access_token: localStorage.getItem('accessToken') || '',
        };
    },

    mutations: {
        login(state, payload) {
            state.access_token = payload.access_token;
            localStorage.setItem('accessToken', payload.access_token);
        },
        logout(state) {
            state.access_token = '';
            localStorage.removeItem('accessToken');
            localStorage.removeItem('selectedPage');
        },
    },

    actions: {
        login(context, payload) {
            context.commit('login', payload);
        },
        logout(context) {
            context.commit('logout');
        },
    },

    getters: {
        getAccessToken(state) {
            return state.access_token;
        },
    },
};

export default store;
