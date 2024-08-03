// store/modules/chat.js

const state = {
  messages: []
};

const mutations = {
  ADD_MESSAGE(state, message) {
    state.messages.push(message);
  }
};

const actions = {
  addMessage({ commit }, message) {
    commit('ADD_MESSAGE', message);
  }
};

const getters = {
  messages: state => state.messages
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
