import ACCESSENUM from "@/access/AccessEnum";

/**
 * 检查指定用户是否有指定权限
 * @param user 用户
 * @param access 权限，默认是未登录
 * @return Boolean 是否有权限
 */
const checkUserAccess = (user: any, access = ACCESSENUM.NOT_LOGIN) => {
  const userRole = user.role;
  if (access === ACCESSENUM.ADMIN && userRole !== ACCESSENUM.ADMIN) {
    return false;
  }
  if (access === ACCESSENUM.USER && userRole === ACCESSENUM.NOT_LOGIN) {
    return false;
  }
  return true;
};

export default checkUserAccess;
