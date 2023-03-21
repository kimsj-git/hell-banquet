import http from "./https.js";

const api = http;


async function getBoardList(parameters, success, fail) {
    const { lastBoardId, size } = parameters
    const res = await api.get(`/boards`, {params: {lastBoardId: lastBoardId, size: size}}).then(success).catch(fail);
    return res
}


export { getBoardList }