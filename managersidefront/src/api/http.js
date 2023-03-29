import axios from "axios";

// axios 객체 생성
export default axios.create({
  // baseURL: "https://ssafy.cossafyco.kro.kr/api/",
  baseURL: "http://j8a802.p.ssafy.io:8030/",

  headers: {
    "Content-Type": "application/json",
  },
});