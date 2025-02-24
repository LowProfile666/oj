import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  state: {
    loginUser: {
      id: -1,
      username: undefined,
      role: undefined,
    },
  },
  mutations: {
    updateLoginUser(state, payload) {
      state.loginUser = payload;
    },
  },
  actions: {
    login({ commit, state }, payload) {
      // todo 登录
      commit("updateLoginUser", payload);
    },
  },
} as StoreOptions<any>;
