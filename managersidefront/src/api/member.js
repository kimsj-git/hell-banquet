import http from "./http.js";

const api = http;

async function login(user, success, fail) {
    await api.post(`/auth/login`, JSON.stringify(user)).then(success).catch(fail);
  }

async function signup(user, success, fail) {
    await api.post(`/users/register`, JSON.stringify(user)).then(success).catch(fail)
}

export { login, signup, }