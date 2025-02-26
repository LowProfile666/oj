import { StoreOptions } from "vuex";
import { UserControllerService } from "../../../generated";
import ACCESSENUM from "@/access/AccessEnum";
import { Message } from "@arco-design/web-vue";

export default {
  namespaced: true,
  state: {
    loginUser: {
      id: -1,
      userName: undefined,
      userRole: undefined,
    },
  },
  mutations: {
    updateLoginUser(state, payload) {
      state.loginUser = payload;
    },
  },
  actions: {
    async login({ commit, state }, payload) {
      const res = await UserControllerService.userLoginUsingPost(payload);
      if (res.code === 0) {
        Message.success("登录成功");
        commit("updateLoginUser", res.data);
        return true;
      } else {
        Message.error("登录失败, " + res.message);
        return false;
      }
    },
    async getLoginUser({ commit, state }) {
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateLoginUser", res.data);
      } else {
        commit("updateLoginUser", {
          id: -1,
          username: undefined,
          role: ACCESSENUM.NOT_LOGIN,
        });
      }
      return state.loginUser;
    },
  },
} as StoreOptions<any>;
