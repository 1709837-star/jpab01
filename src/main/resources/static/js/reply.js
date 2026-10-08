/* 댓글 기능에서 브라우저 <-> Spring 서버를 연결해주는 중간 역할 */
/* ★★★ ReplyController에 있는 API들(register, getList ..)을 JavaScript 함수로 만들어 놓은 것 */

/* 구조 : read.html -> getList() -> reply.js -> axios.get() -> ReplyController */
// * async function : "이 함수 안에서는 서버 통신같은 비동기 작업을 할거야."
// * await : "서버에서 응답이 올 때까지 기다렸다가 다음 줄로 가자."


/* ★ 특정 게시글의 댓글 목록을 가져오는 것 (테스트용) */
async function get1(bno) {

    const result = await axios.get(`/replies/list/${bno}`)
    // -> bno=10; 이면 /replies/list/10이 됨
    return result;
}

/* ★ 댓글 목록을 가져오는 실제 핵심 함수 */
async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})
    // -> 대략 GET /replies/list/10?page=1&size=10 형태가 됨
    // Controller의 @PathVariable("bno") Long bno에서 10 가져오고
    // PageRequestDTO pageRequestDTO ?page=1&size=10을 받아서
    // PageRequestDTO.getPage() .getSize()처럼 사용

    // "댓글을 새로 등록한 직후 마지막 페이지의 (new)댓글을 보여줘야 하는 상황이면"
    if(goLast){
        const total = result.data.total // 서버에서 받은 응답이 result에 들어옴, total -> 총 댓글 수
        const lastPage = parseInt(Math.ceil(total/size)) // -> 마지막 페이지 계산

        return getList({bno:bno, page:lastPage, size:size})
        // ex) getList({bno: 10, page:1, size: 10, goLast: true})
        // 처음에는 /replies/list/10?page=1&size=10 요청 -> 총 댓글 23개 확인 -> 마지막 페이지 = 3
        // ★★★다시 getList({bno: 10, page: 3, size: 10}) -> replies/list/10?page=3&size=10 요청
    }

    return result.data
}


/* ★ 댓글 등록 함수 */
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`,replyObj)
    // -> Controller에서 @RequestBody ReplyDTO replyDTO가 여기서 보낸 JSON을 ReplyDTO 객체로 변환
    return response.data
}


/* ★ 댓글 하나 조회 함수 */
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    // getReply(5) -> GET /replies/5 -> Controller -> (...)
    // -> DB에서 5번째 댓글 찾기 -> ReplyDTO로 변환 -> JSON으로 브라우저에 전달
    return response.data
}


/* ★ 수정 함수 */
async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    // PUT /replies/5 -> Controller의 replyDTO.setRno(rno); -> DTO의 rno=5 이렇게 만들어 줌
    return response.data
}


/* ★ 삭제 함수 */
async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    // DELETE /replies/5 -> Controller @DeleteMapping -> reply.Service.remove(rno)
    // -> replyRepository.deleteByID(rno) -> 5번 댓글 삭제
    return response.data
}