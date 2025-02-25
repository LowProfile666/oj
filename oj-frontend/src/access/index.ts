import ACCESSENUM from "@/access/AccessEnum";
import router from "@/router";
import store from "@/store";
import AccessEnum from "@/access/AccessEnum";
import checkUserAccess from "@/access/check";

router.beforeEach(async (to, from, next) => {
  const pageAccess = (to.meta.access as string) ?? ACCESSENUM.NOT_LOGIN;
  // 页面不需要权限，不需要登录
  if (pageAccess === ACCESSENUM.NOT_LOGIN) {
    next();
    return;
  }

  const loginUser = await store.dispatch("user/getLoginUser");

  // 页面需要登录，用户没有登录，跳转到登录页面
  if (
    pageAccess !== AccessEnum.NOT_LOGIN &&
    loginUser.role === AccessEnum.NOT_LOGIN
  ) {
    next(`/user/login?redirectName=${to.name as string}`);
    return;
  }
  // 页面需要管理员权限，用户没有管理员权限，跳转到的无权限页面
  if (checkUserAccess(loginUser, pageAccess)) {
    next("/403");
    return;
  }
  next();
});
