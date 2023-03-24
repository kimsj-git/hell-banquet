import http from "./https.js";

const api = http;

async function login(user, success, fail) {
  await api.post(`/auth/login`, JSON.stringify(user)).then(success).catch(fail);
}

async function getUserInfo(user, success, fail) {
  const res = await api.get(`/auth/info/${user}`).then(success).catch(fail);
  return res
}


// url이 auth인지 users인지 모르겠어용
async function signup(user, success, fail) {
  await api.post(`/auth/register`, JSON.stringify(user)).then(success).catch(fail);
}

async function updateUserInfo(user, success, fail) {
  await api.put(`/auth/info/${user.userId}`, JSON.stringify(user)).then(success).catch(fail);
}

async function updateUserPassword(user, success, fail) {
  await api.put(`/auth/pw`, JSON.stringify(user)).then(success).catch(fail);
}

async function deleteUser(user, success, fail) {
  await api.delete(`/auth/info/${user.id}`).then(success).catch(fail);
}

async function userFirst(user, success, fail) {
  await api.delete(`/auth/visited`, JSON.stringify(user)).then(success).catch(fail);
}


export { login, signup, getUserInfo, updateUserInfo, updateUserPassword, deleteUser, userFirst }