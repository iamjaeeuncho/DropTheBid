// search_store.js
// 작성자: 이주윤

import axios from 'axios';

const initialState = {
    searchResults: {},
    searchParams: {
        q: '',
        page: 1,
        category: null,
        condition: null,
        minPrice: null,
        maxPrice: null,
        sort: null,
    },
    isSearching: false,
};

export default {
    namespaced: true,
    state: { ...initialState },
    mutations: {
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results;
        },
        SET_SEARCH_PARAMS(state, params) {
            state.searchParams = { ...state.searchParams, ...params };
        },
        SET_IS_SEARCHING(state, isSearching) {
            state.isSearching = isSearching;
        },
        SET_LAST_SEARCH_PARAMS(state, params) {
            state.lastSearchParams = params;
        },
        RESET_STATE(state) {
            Object.assign(state, initialState);
        },
        CLEAR_CATEGORY(state) {
            state.searchParams.category = null;
        },
        RESET_FILTERS(state) {
            state.searchParams = {
                ...state.searchParams,
                category: null,
                condition: null,
                minPrice: null,
                maxPrice: null,
                sort: null,
            };
        },
    },
    actions: {
        setSearchParams({ commit, dispatch }, params) {
            commit('SET_SEARCH_PARAMS', params);
            return dispatch('searchAuctions');
        },

        async searchAuctions({ commit, state }) {
            if (state.isSearching) return; // Prevent multiple simultaneous searches

            const currentParams = JSON.stringify(state.searchParams);
            if (currentParams === state.lastSearchParams) return; // Prevent duplicate searches

            commit('SET_IS_SEARCHING', true);
            commit('SET_LAST_SEARCH_PARAMS', currentParams);

            try {
                const response = await axios.get(`/search`, {
                    params: state.searchParams,
                });
                commit('SET_SEARCH_RESULTS', response.data);
            } catch (error) {
                console.error('검색 중 오류 발생:', error);
            } finally {
                commit('SET_IS_SEARCHING', false);
            }
        },

        resetSearchState({ commit }) {
            commit('RESET_STATE');
        },

        setCategory({ commit, dispatch }, category) {
            commit('SET_SEARCH_PARAMS', { category });
            return dispatch('searchAuctions');
        },

        clearCategory({ commit }) {
            commit('CLEAR_CATEGORY');
        },

        resetFilters({ commit }) {
            commit('RESET_FILTERS');
        },

        newSearch({ commit, dispatch }, query) {
            commit('RESET_FILTERS');
            commit('SET_SEARCH_PARAMS', { q: query, page: 1 });
            return dispatch('searchAuctions');
        },
    },
    getters: {
        getSearchResults: (state) => state.searchResults,
        getSearchParams: (state) => state.searchParams,
        getSelectedCategory: (state) => state.searchParams.category,
    },
};
